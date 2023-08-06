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
import pl.pawc.ewi.repository.KilometersRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;
import pl.pawc.ewi.repository.FuelConsumptionRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
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

    public List<Document> getDokumenty(int year, int month){
        List<Document> documents = documentRepository.getDocuments(year, month);
        Set<Machine> collect = documents.stream().map(Document::getMachine).collect(Collectors.toSet());
        for(Machine m : collect){
            if(m.getCategories() != null) m.getCategories().forEach(c -> c.setMachines(null));
        }
        return documents;
    }

    public Optional<Document> findById(String numer){
        return documentRepository.findById(numer);
    }

    public BigDecimal getSumaKilometry(String maszynaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException {

        final Document document;
        Calendar calD = Calendar.getInstance();
        if(excludedDocNumber != null){
            document = documentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException((excludedDocNumber)));
            calD.setTime(document.getDate());
        }
        else{
            document = null;
        }

        Machine machine = new Machine();
        machine.setId(maszynaId);

        Calendar cal = Calendar.getInstance();

        Kilometers kilometers = kilometersRepository.findOneByMachineAndYearAndMonth(machine, year, month);
        BigDecimal stan = kilometers != null ? kilometers.getValue() : BigDecimal.ZERO;

        BigDecimal reduce = documentRepository.findByMachine(machine).stream().filter(d -> {
            cal.setTime(d.getDate());
            return (cal.get(Calendar.MONTH) + 1) == month
                    && cal.get(Calendar.YEAR) == year
                    && !d.getNumber().equals(excludedDocNumber)
                    && (document == null || calD.get(Calendar.DAY_OF_MONTH) >= cal.get(Calendar.DAY_OF_MONTH));
        }).map(Document::getKilometers).reduce(BigDecimal.ZERO, BigDecimal::add);

        return stan.add(reduce);

    }

    public Document get(String numer) {

        Optional<Document> result = documentRepository.findById(numer);
        Document document = null;

        if(result.isPresent()){
            document = result.get();

            Calendar cal = Calendar.getInstance();
            cal.setTime(document.getDate());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            List<FuelConsumption> zuzycia = fuelConsumptionRepository.findByDocument(document);
            document.setFuelConsumption(zuzycia);

            for(FuelConsumption fuelConsumption : zuzycia){

                BigDecimal suma = null;
                BigDecimal sumaBefore = null;
                fuelConsumption.setDocument(document);
                try {
                    suma = fuelConsumptionService.getSuma(fuelConsumption.getFuelConsumptionStandard().getId(), year, month, null);
                    sumaBefore = fuelConsumptionService.getSuma(fuelConsumption.getFuelConsumptionStandard().getId(), year, month, document.getNumber());

                } catch (DocumentNotFoundException | FuelConsumptionStandardNotFoundException e) {
                    e.printStackTrace();
                }

                FuelInitialState fuelInitialState = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelConsumption.getFuelConsumptionStandard(), year, month);
                BigDecimal stanD = fuelInitialState == null ? BigDecimal.ZERO : fuelInitialState.getValue();

                fuelConsumption.getFuelConsumptionStandard().setSum(suma);
                fuelConsumption.getFuelConsumptionStandard().setSumBefore(sumaBefore);
                fuelConsumption.getFuelConsumptionStandard().setInitialState(stanD);

                fuelConsumption.setDocument(null);
                fuelConsumption.getFuelConsumptionStandard().setMachine(null);

            }

            BigDecimal kilometryBefore = null;
            try {
                kilometryBefore = getSumaKilometry(document.getMachine().getId(), year, month, document.getNumber());
            } catch (DocumentNotFoundException e) {
                e.printStackTrace();
            }
            document.setKilometersBefore(kilometryBefore);
            Set<Category> categories = document.getMachine().getCategories();
            if(categories != null) categories.forEach(c -> c.setMachines(null));
        }

        return document;

    }

    public void post(Document document){
        if(document.getKilometers() == null) document.setKilometers(BigDecimal.ZERO);
        if(document.getKilometersTrailer() == null) document.setKilometersTrailer(BigDecimal.ZERO);
        for(FuelConsumption fuelConsumption : document.getFuelConsumption()){
            if(fuelConsumption.getValue() == null) fuelConsumption.setValue(BigDecimal.ZERO);
            if(fuelConsumption.getRefueled() == null) fuelConsumption.setRefueled(BigDecimal.ZERO);
            if(fuelConsumption.getHeating() == null) fuelConsumption.setHeating(BigDecimal.ZERO);
            fuelConsumption.setDocument(document);
            fuelConsumptionRepository.save(fuelConsumption);
        }
    }

    public void put(Document document) throws DocumentNotFoundException {
        Optional<Document> byId = findById(document.getNumber());
        if(byId.isEmpty()) throw new DocumentNotFoundException(document.getNumber());

        if(document.getKilometers() == null) document.setKilometers(BigDecimal.ZERO);
        if(document.getKilometersTrailer() == null) document.setKilometersTrailer(BigDecimal.ZERO);

        Document documentDB = byId.get();

        documentDB.setDate(document.getDate());
        documentDB.setKilometers(document.getKilometers());
        documentDB.setKilometersTrailer(document.getKilometersTrailer());
        documentRepository.save(documentDB);

        for(FuelConsumption fuelConsumption : document.getFuelConsumption()) {
            FuelConsumption fuelConsumptionDB = fuelConsumptionRepository.findById(fuelConsumption.getId()).orElse(null);
            if(fuelConsumptionDB == null) continue;
            if(fuelConsumption.getValue() == null) fuelConsumption.setValue(BigDecimal.ZERO);
            if(fuelConsumption.getRefueled() == null) fuelConsumption.setRefueled(BigDecimal.ZERO);
            if(fuelConsumption.getHeating() == null) fuelConsumption.setHeating(BigDecimal.ZERO);
            fuelConsumptionDB.setValue(fuelConsumption.getValue());
            fuelConsumptionDB.setRefueled(fuelConsumption.getRefueled());
            fuelConsumptionDB.setHeating(fuelConsumption.getHeating());

            fuelConsumptionRepository.save(fuelConsumptionDB);
        }
    }

    public void delete(String numer) {

        documentRepository.findById(numer).ifPresent(dok -> {
            fuelConsumptionRepository.findByDocument(dok).forEach(fuelConsumptionRepository::delete);
            documentRepository.delete(dok);
        });

    }

}