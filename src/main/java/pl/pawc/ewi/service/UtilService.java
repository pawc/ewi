package pl.pawc.ewi.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UtilService {

    public BigDecimal[] calculate(BigDecimal before, BigDecimal fuelConsumptionStandard,
                                  BigDecimal fuelConsumptionStandardVal,
                                  BigDecimal heating, BigDecimal refueled, boolean isRounded){

        int scale = isRounded ? 2 : 1;

        BigDecimal fuelConsumption = fuelConsumptionStandard
                .multiply(fuelConsumptionStandardVal)
                .setScale(scale, RoundingMode.HALF_UP);

        return new BigDecimal[]{
                before
                    .subtract(fuelConsumption)
                    .subtract(heating)
                    .add(refueled)
                    .setScale(scale, RoundingMode.HALF_UP),
                fuelConsumption
        };

    }

}