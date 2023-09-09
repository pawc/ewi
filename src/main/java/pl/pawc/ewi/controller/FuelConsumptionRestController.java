package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.service.UtilService;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class FuelConsumptionRestController {

    private final UtilService utilService;

    @RequestMapping("/calc")
    public BigDecimal[] calc(
            @RequestParam("before") BigDecimal before,
            @RequestParam("fuelConsumptionStandard") BigDecimal fuelConsumptionStandard,
            @RequestParam("fuelConsumptionStandardVal") BigDecimal fuelConsumptionStandardVal,
            @RequestParam(name = "heating", required = false) BigDecimal heating,
            @RequestParam(name = "refueled", required = false) BigDecimal refueled,
            @RequestParam(name = "isRounded", required = false) boolean isRounded){

        if(heating == null) heating = BigDecimal.ZERO;
        if(refueled == null) refueled = BigDecimal.ZERO;

        return utilService.calculate(before, fuelConsumptionStandard, fuelConsumptionStandardVal, heating, refueled, isRounded);

    }

}