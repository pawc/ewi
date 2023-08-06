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

    @PutMapping("stan")
    public void stanPut(
            @RequestBody FuelInitialState fuelInitialState) {

        if(fuelInitialStateService.post(fuelInitialState)) logger.info("/stan dodano {} - normaID={}", fuelInitialState, fuelInitialState.getFuelConsumptionStandard().getId());
        else logger.info("/stan zaktualizowano {} - normaID={}", fuelInitialState, fuelInitialState.getFuelConsumptionStandard().getId());
    }

    @PostMapping("stany")
    public void stanyPost(
            @RequestBody List<FuelInitialState> stany) {

        fuelInitialStateService.stanyPost(stany);
        for(FuelInitialState fuelInitialState : stany){
            logger.info("/stany {}-{} - normaID={}", fuelInitialState.getYear(), fuelInitialState.getMonth(), fuelInitialState.getFuelConsumptionStandard().getId());
        }
    }

    @GetMapping("stanyGet")
    public List<FuelInitialStateReport> stanyGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/stanyGet {}-{}", rok, miesiac);
        return fuelInitialStateService.findBy(rok, miesiac);

    }

}