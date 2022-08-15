package pl.pawc.ewi.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UtilService {

    public BigDecimal[] calc(BigDecimal before, BigDecimal norma, BigDecimal normaVal,
                 BigDecimal ogrzewanie, BigDecimal tankowanie, boolean czyZaokr1setna){

        int scale = czyZaokr1setna ? 2 : 1;

        BigDecimal zuzycie = norma.multiply(normaVal).setScale(scale, RoundingMode.HALF_UP);

        return new BigDecimal[]{
                before.subtract(zuzycie).subtract(ogrzewanie).add(tankowanie).setScale(scale, RoundingMode.HALF_UP),
                zuzycie
        };

    }

}