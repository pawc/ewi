package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.model.FuelInitialStateReport;
import pl.pawc.ewi.service.FuelInitialStateService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FuelInitialStateRestController {

    private static final Logger logger = LogManager.getLogger(FuelInitialStateRestController.class);
    private final FuelInitialStateService fuelInitialStateService;

    @PutMapping("fuelInitialState")
    public void fuelInitialStatePut(
            @RequestBody FuelInitialState fuelInitialState) {

        if(fuelInitialStateService.post(fuelInitialState)) logger.info("/fuelInitialState added {} - fuelConsumptionStandardId={}",
                fuelInitialState, fuelInitialState.getFuelConsumptionStandard().getId());
        else logger.info("/fuelInitialState updated {} - fuelConsumptionStandardId={}",
                fuelInitialState, fuelInitialState.getFuelConsumptionStandard().getId());
    }

    @PostMapping("fuelInitialStates")
    public void fuelInitialStatesPost(
            @RequestBody List<FuelInitialState> fuelInitialStates) {

        fuelInitialStateService.stanyPost(fuelInitialStates);
        for(FuelInitialState fuelInitialState : fuelInitialStates){
            logger.info("/fuelInitialStates {}-{} - fuelConsumptionStandardId={}",
                    fuelInitialState.getYear(), fuelInitialState.getMonth(),
                    fuelInitialState.getFuelConsumptionStandard().getId());
        }
    }

    @GetMapping("fuelInitialStateReport")
    public List<FuelInitialStateReport> fuelInitialStateReport(
            @RequestParam("year") int year,
            @RequestParam("month") int month){

        logger.info("/fuelInitialState {}-{}", year, month);
        return fuelInitialStateService.findBy(year, month);

    }

}