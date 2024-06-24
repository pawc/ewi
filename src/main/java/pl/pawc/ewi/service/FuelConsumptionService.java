package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.FuelConsumption;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.FuelConsumptionStandardNotFoundException;
import pl.pawc.ewi.repository.DocumentRepository;
import pl.pawc.ewi.repository.FuelConsumptionRepository;
import pl.pawc.ewi.repository.FuelConsumptionStandardRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FuelConsumptionService {

    private final FuelConsumptionRepository fuelConsumptionRepository;
    private final FuelInitialStateRepository fuelInitialStateRepository;
    private final DocumentRepository documentRepository;
    private final FuelConsumptionStandardRepository fuelConsumptionStandardRepository;

    public BigDecimal getSum(long fuelConsumptionStandardId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException, FuelConsumptionStandardNotFoundException {

        Document excludedDocument;

        if(excludedDocNumber != null){
            excludedDocument = documentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException(excludedDocNumber));
            Calendar calD = Calendar.getInstance();
            calD.setTime(excludedDocument.getDate());
        }
        else {
            excludedDocument = null;
        }

        Optional<FuelConsumptionStandard> fuelConsumptionStandardById = fuelConsumptionStandardRepository.findById(fuelConsumptionStandardId);
        if(fuelConsumptionStandardById.isEmpty()) throw new FuelConsumptionStandardNotFoundException(fuelConsumptionStandardId);
        FuelConsumptionStandard fuelConsumptionStandard = fuelConsumptionStandardById.get();

        Calendar cal = Calendar.getInstance();

        List<FuelConsumption> collect = fuelConsumptionRepository.findByFuelConsumptionStandard(fuelConsumptionStandard).stream()
                .filter(z -> {
                        cal.setTime(z.getDocument().getDate());
                        return cal.get(Calendar.MONTH) + 1 == month
                            && cal.get(Calendar.YEAR) == year
                            && (excludedDocument == null || !z.getDocument().getNumber().equals(excludedDocNumber))
                            && (excludedDocument == null || excludedDocument.getDate().compareTo(z.getDocument().getDate()) > 0);
                    }
                ).toList();

        Optional<FuelInitialState> fuelInitialStateOptional = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelConsumptionStandard, year, month);
        FuelInitialState fuelInitialState;
        if (fuelInitialStateOptional.isEmpty()){
            fuelInitialState = new FuelInitialState();
            fuelInitialState.setValue(BigDecimal.ZERO);
        }
        else {
            fuelInitialState = fuelInitialStateOptional.get();
        }

        BigDecimal refueled = collect.stream().map(FuelConsumption::getRefueled).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal heating = collect.stream().map(FuelConsumption::getHeating).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sum = collect.stream()
                .map(z -> (z.getValue().multiply(z.getFuelConsumptionStandard().getValue()))
                    .setScale(fuelConsumptionStandard.isRounded() ? 2 : 1, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return (fuelInitialState.getValue().add(refueled).subtract(heating).subtract(sum))
                .setScale(fuelConsumptionStandard.isRounded() ? 2 : 1, RoundingMode.HALF_UP);

    }

    public BigDecimal getSumYear(long fuelConsumptionStandardId, int year){

        FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
        fuelConsumptionStandard.setId(fuelConsumptionStandardId);
        Calendar cal = Calendar.getInstance();

        List<FuelConsumption> collect = fuelConsumptionRepository.findByFuelConsumptionStandard(fuelConsumptionStandard)
                .stream()
                .filter(z -> {
                            cal.setTime(z.getDocument().getDate());
                            return cal.get(Calendar.YEAR) == year;
                        }
                ).toList();

        return collect.stream().map(z ->
                z.getValue().multiply(z.getFuelConsumptionStandard().getValue()).setScale(2, RoundingMode.HALF_UP)
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

}