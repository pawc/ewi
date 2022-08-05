package pl.pawc.ewi.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UtilService {

    public BigDecimal[] calc(BigDecimal before, BigDecimal norma, BigDecimal normaVal,
                 BigDecimal ogrzewanie, BigDecimal tankowanie){

        BigDecimal zuzycie = norma.multiply(normaVal).setScale(1, RoundingMode.HALF_UP);

        return new BigDecimal[]{
                before.subtract(zuzycie).subtract(ogrzewanie).add(tankowanie).setScale(1, RoundingMode.HALF_UP),
                zuzycie
        };

    }

}