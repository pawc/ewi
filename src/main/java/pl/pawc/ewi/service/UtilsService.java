package pl.pawc.ewi.service;

import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
public class UtilsService {


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