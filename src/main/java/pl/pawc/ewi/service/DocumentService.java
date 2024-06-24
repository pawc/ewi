package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.FuelConsumption;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.FuelConsumptionStandardNotFoundException;
import pl.pawc.ewi.repository.DocumentRepository;
import pl.pawc.ewi.repository.FuelConsumptionRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;
import pl.pawc.ewi.repository.KilometersRepository;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final KilometersRepository kilometersRepository;
    private final FuelConsumptionRepository fuelConsumptionRepository;
    private final FuelInitialStateRepository fuelInitialStateRepository;
    private final FuelConsumptionService fuelConsumptionService;

    public List<Document> getDocuments(int year, int month){
        List<Document> documents = documentRepository.getDocuments(year, month);
        Set<Machine> machines = documents.stream().map(Document::getMachine).collect(Collectors.toSet());
        setNullMachinesInCategories(machines);
        return documents;
    }

    private void setNullMachinesInCategories(Set<Machine> machines) {
        machines.stream()
                .map(Machine::getCategories)
                .filter(Objects::nonNull)
                .forEach(this::setNullMachines);
    }

    private void setNullMachines(Set<Category> categories) {
        categories.forEach(category -> category.setMachines(null));
    }

    public Optional<Document> findById(String numer){
        return documentRepository.findById(numer);
    }

    public BigDecimal getSumKilometers(String machineId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException {
        Machine machine = createMachine(machineId);
        BigDecimal initialKilometers = getInitialKilometers(machine, year, month);
        BigDecimal additionalKilometers = getAdditionalKilometers(machine, year, month, excludedDocNumber);
        return initialKilometers.add(additionalKilometers);
    }

    private Machine createMachine(String machineId) {
        Machine machine = new Machine();
        machine.setId(machineId);
        return machine;
    }

    private BigDecimal getInitialKilometers(Machine machine, int year, int month) {
        return kilometersRepository.findOneByMachineAndYearAndMonth(machine, year, month)
                .map(Kilometers::getValue)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getAdditionalKilometers(Machine machine, int year, int month, String excludedDocNumber) {
        List<Document> documents = documentRepository.findByMachine(machine);
        return documents.stream()
                .filter(d -> shouldIncludeDocument(d, year, month, excludedDocNumber))
                .map(Document::getKilometers)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean shouldIncludeDocument(Document document, int year, int month, String excludedDocNumber) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(document.getDate());
        boolean isSameMonthAndYear = (cal.get(Calendar.MONTH) + 1) == month && cal.get(Calendar.YEAR) == year;
        boolean isNotExcludedDocument = !document.getNumber().equals(excludedDocNumber);
        boolean isBeforeExcludedDocument = isBeforeExcludedDocument(document, excludedDocNumber);
        return isSameMonthAndYear && isNotExcludedDocument && isBeforeExcludedDocument;
    }

    private boolean isBeforeExcludedDocument(Document document, String excludedDocNumber) {
        if (excludedDocNumber == null) {
            return false;
        }
        Optional<Document> documentById = documentRepository.findById(excludedDocNumber);
        if (documentById.isEmpty()) {
            return false;
        }
        Document excludedDocument = documentById.orElse(null);
        Calendar calD = Calendar.getInstance();
        calD.setTime(excludedDocument.getDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(document.getDate());
        return calD.get(Calendar.DAY_OF_MONTH) >= cal.get(Calendar.DAY_OF_MONTH);
    }

    public Document get(String number) throws FuelConsumptionStandardNotFoundException, DocumentNotFoundException {
        Optional<Document> byId = documentRepository.findById(number);
        if (byId.isPresent()) {
            return processDocument(byId.get());
        }
        else {
            throw new DocumentNotFoundException(number);
        }
    }

    private Document processDocument(Document document) throws FuelConsumptionStandardNotFoundException, DocumentNotFoundException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(document.getDate());

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        List<FuelConsumption> fuelConsumptions = fuelConsumptionRepository.findByDocument(document);
        document.setFuelConsumption(fuelConsumptions);

        for (FuelConsumption fuelConsumption : fuelConsumptions) {
            setDefaultValuesForFuelConsumption(fuelConsumption);
            fuelConsumption.setDocument(document);
            setFuelConsumptionStandardValues(fuelConsumption, year, month, document.getNumber());
        }

        BigDecimal kilometersBefore = getSumKilometers(document.getMachine().getId(), year, month, document.getNumber());
        document.setKilometersBefore(kilometersBefore);
        setNullMachinesInCategories(Set.of(document.getMachine()));

        return document;
    }

    private void setFuelConsumptionStandardValues(FuelConsumption fuelConsumption, int year, int month, String documentNumber) throws FuelConsumptionStandardNotFoundException, DocumentNotFoundException {
        BigDecimal sum = fuelConsumptionService.getSum(fuelConsumption.getFuelConsumptionStandard().getId(), year, month, null);
        BigDecimal sumBefore = fuelConsumptionService.getSum(fuelConsumption.getFuelConsumptionStandard().getId(), year, month, documentNumber);
        FuelInitialState fuelInitialState = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelConsumption.getFuelConsumptionStandard(), year, month);
        BigDecimal initialState = fuelInitialState == null ? BigDecimal.ZERO : fuelInitialState.getValue();

        fuelConsumption.getFuelConsumptionStandard().setSum(sum);
        fuelConsumption.getFuelConsumptionStandard().setSumBefore(sumBefore);
        fuelConsumption.getFuelConsumptionStandard().setInitialState(initialState);

        fuelConsumption.setDocument(null);
        fuelConsumption.getFuelConsumptionStandard().setMachine(null);
    }

    public void post(Document document){
        setDefaultValuesForDocument(document);
        saveFuelConsumption(document);
    }

    private void setDefaultValuesForDocument(Document document) {
        if(document.getKilometers() == null) document.setKilometers(BigDecimal.ZERO);
        if(document.getKilometersTrailer() == null) document.setKilometersTrailer(BigDecimal.ZERO);
    }

    private void saveFuelConsumption(Document document) {
        for(FuelConsumption fuelConsumption : document.getFuelConsumption()){
            setDefaultValuesForFuelConsumption(fuelConsumption);
            fuelConsumption.setDocument(document);
            fuelConsumptionRepository.save(fuelConsumption);
        }
    }

    private void setDefaultValuesForFuelConsumption(FuelConsumption fuelConsumption) {
        if(fuelConsumption.getValue() == null) fuelConsumption.setValue(BigDecimal.ZERO);
        if(fuelConsumption.getRefueled() == null) fuelConsumption.setRefueled(BigDecimal.ZERO);
        if(fuelConsumption.getHeating() == null) fuelConsumption.setHeating(BigDecimal.ZERO);
    }

    public void put(Document document) throws DocumentNotFoundException {
        Document documentDB = findDocumentById(document.getNumber());
        setDefaultValuesForDocument(document);
        updateDocument(document, documentDB);
        document.getFuelConsumption().forEach(this::updateFuelConsumption);
    }

    private Document findDocumentById(String documentNumber) throws DocumentNotFoundException {
        Optional<Document> byId = findById(documentNumber);
        if(byId.isEmpty()) throw new DocumentNotFoundException(documentNumber);
        return byId.get();
    }

    private void updateDocument(Document document, Document documentDB) {
        documentDB.setDate(document.getDate());
        documentDB.setKilometers(document.getKilometers());
        documentDB.setKilometersTrailer(document.getKilometersTrailer());
        documentRepository.save(documentDB);
    }

    private void updateFuelConsumption(FuelConsumption fuelConsumption) {
        Optional<FuelConsumption> fuelConsumptionOptional = fuelConsumptionRepository.findById(fuelConsumption.getId());
        if(fuelConsumptionOptional.isEmpty()) return;
        FuelConsumption fuelConsumptionDB = fuelConsumptionOptional.get();
        setDefaultValuesForFuelConsumption(fuelConsumption);
        fuelConsumptionDB.setValue(fuelConsumption.getValue());
        fuelConsumptionDB.setRefueled(fuelConsumption.getRefueled());
        fuelConsumptionDB.setHeating(fuelConsumption.getHeating());
        fuelConsumptionRepository.save(fuelConsumptionDB);
    }

    public void delete(String numer) {
        documentRepository.findById(numer).ifPresent(dok -> {
            fuelConsumptionRepository.findByDocument(dok).forEach(fuelConsumptionRepository::delete);
            documentRepository.delete(dok);
        });
    }

}