package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.FuelConsumption;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.FuelConsumptionStandardNotFoundException;
import pl.pawc.ewi.repository.DocumentRepository;
import pl.pawc.ewi.repository.FuelConsumptionStandardRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;
import pl.pawc.ewi.repository.FuelConsumptionRepository;

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

    public BigDecimal getSum(long normaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException, FuelConsumptionStandardNotFoundException {

        Document document;

        if(excludedDocNumber != null){
            document = documentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException(excludedDocNumber));
            Calendar calD = Calendar.getInstance();
            calD.setTime(document.getDate());
        }
        else {
            document = null;
        }

        Optional<FuelConsumptionStandard> fuelConsumptionStandardById = fuelConsumptionStandardRepository.findById(normaId);
        if(fuelConsumptionStandardById.isEmpty()) throw new FuelConsumptionStandardNotFoundException(normaId);
        FuelConsumptionStandard fuelConsumptionStandard = fuelConsumptionStandardById.get();

        Calendar cal = Calendar.getInstance();

        List<FuelConsumption> collect = fuelConsumptionRepository.findByFuelConsumptionStandard(fuelConsumptionStandard).stream()
                .filter(z -> {
                        cal.setTime(z.getDocument().getDate());
                        return cal.get(Calendar.MONTH) + 1 == month
                            && cal.get(Calendar.YEAR) == year
                            && (document == null || !z.getDocument().getNumber().equals(excludedDocNumber))
                            && (document == null || document.getDate().compareTo(z.getDocument().getDate()) > 0);
                    }
                ).toList();

        FuelInitialState fuelInitialState = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelConsumptionStandard, year, month);

        if(fuelInitialState == null){
            fuelInitialState = new FuelInitialState();
            fuelInitialState.setValue(BigDecimal.ZERO);
        }

        BigDecimal zatankowano = collect.stream().map(FuelConsumption::getRefueled).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ogrzewanie = collect.stream().map(FuelConsumption::getHeating).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sum = collect.stream()
                .map(z -> (z.getValue().multiply(z.getFuelConsumptionStandard().getValue())).setScale(fuelConsumptionStandard.isRounded() ? 2 : 1, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return (fuelInitialState.getValue().add(zatankowano).subtract(ogrzewanie).subtract(sum)).setScale(fuelConsumptionStandard.isRounded() ? 2 : 1, RoundingMode.HALF_UP);

    }

    public BigDecimal getSumYear(long normaId, int year){

        FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
        fuelConsumptionStandard.setId(normaId);
        Calendar cal = Calendar.getInstance();

        List<FuelConsumption> collect = fuelConsumptionRepository.findByFuelConsumptionStandard(fuelConsumptionStandard).stream()
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