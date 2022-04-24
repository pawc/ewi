package pl.pawc.ewi.service;

import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@NoArgsConstructor
public class UtilsService {

    BigDecimal multiply(double a, double b, boolean precision){
        BigDecimal a1 = new BigDecimal(String.valueOf(a));
        BigDecimal b1 = new BigDecimal(String.valueOf(b));
        BigDecimal c = a1.multiply(b1);

        if(precision) return c.setScale(2, RoundingMode.HALF_UP);
        else return c.setScale(1, RoundingMode.HALF_UP);
    }


    double myRound(double value, boolean precisionMode) {
        if(precisionMode) return Precision.round(value, 2, BigDecimal.ROUND_HALF_UP);
        else {
            double floor = Math.floor(value * 10)/10;
            double rest = value - floor;
            if(Math.abs(rest - 0.05) < 0.0001) return floor + 0.1;
            else if (rest > 0.05) return floor + 0.1;
            else return floor;
        }

    }

}