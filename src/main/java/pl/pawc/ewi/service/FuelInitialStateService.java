package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.model.FuelInitialStateReport;
import pl.pawc.ewi.repository.FuelConsumptionStandardRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FuelInitialStateService {

    private static final Logger logger = LogManager.getLogger(FuelInitialStateService.class);
    private final FuelInitialStateRepository fuelInitialStateRepository;
    private final FuelConsumptionStandardRepository fuelConsumptionStandardRepository;

    public List<FuelInitialStateReport> findBy(int year, int month) {

        List<FuelInitialStateReport> result = new ArrayList<>();

        fuelConsumptionStandardRepository.findAll().forEach(n -> {
            FuelInitialStateReport fuelInitialStateReport = new FuelInitialStateReport();
            fuelInitialStateReport.setMachineName(n.getMachine().getName());
            fuelInitialStateReport.setMachineId(n.getMachine().getId());
            String unit = n.getUnitObj() == null ? n.getUnit() : n.getUnitObj().getName();
            fuelInitialStateReport.setUnit(unit);
            fuelInitialStateReport.setFuelConsumptionStandardId(n.getId());
            FuelInitialState fuelInitialState = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(n, year, month);
            if(fuelInitialState == null) {
                fuelInitialState = new FuelInitialState();
                fuelInitialState.setId(-1);
                fuelInitialState.setValue(BigDecimal.ZERO);
            }
            fuelInitialStateReport.setFuelInitialStateId(fuelInitialState.getId());
            fuelInitialStateReport.setFuelInitialState(fuelInitialState.getValue());
            result.add(fuelInitialStateReport);
        });

        return result;

    }

    public boolean post(FuelInitialState fuelInitialState){
        FuelInitialState fuelInitialStateDB = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelInitialState.getFuelConsumptionStandard(), fuelInitialState.getYear(), fuelInitialState.getMonth());
        if(fuelInitialState.getValue() == null) fuelInitialState.setValue(BigDecimal.ZERO);
        if(fuelInitialStateDB == null){
            fuelInitialStateRepository.save(fuelInitialState);
            return true;
        }
        else{
            fuelInitialStateDB.setValue(fuelInitialState.getValue());
            fuelInitialStateRepository.save(fuelInitialStateDB);
            return false;
        }
    }

    public void postFuelInitialStates(List<FuelInitialState> initialStates){
        FuelInitialState fuelInitialStateDB;
        for(FuelInitialState fuelInitialState : initialStates){

            Optional<FuelConsumptionStandard> byId = fuelConsumptionStandardRepository.findById(fuelInitialState.getFuelConsumptionStandard().getId());
            if(byId.isPresent() && !byId.get().getMachine().isCarriedOver()) continue;

            fuelInitialStateDB = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelInitialState.getFuelConsumptionStandard(), fuelInitialState.getYear(), fuelInitialState.getMonth());
            if(fuelInitialState.getValue() == null) fuelInitialState.setValue(BigDecimal.ZERO);
            if(fuelInitialStateDB == null){
                fuelInitialStateRepository.save(fuelInitialState);
            }
            else{
                fuelInitialStateDB.setValue(fuelInitialState.getValue());
                fuelInitialStateRepository.save(fuelInitialStateDB);
            }
        }
    }

}