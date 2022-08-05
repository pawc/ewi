package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@RestController
public class ZuzycieRestController {

    @RequestMapping("/calc")
    public BigDecimal[] calc(
            @RequestParam("before") BigDecimal before,
            @RequestParam("norma") BigDecimal norma,
            @RequestParam("normaVal") BigDecimal normaVal,
            @RequestParam(name = "ogrzewanie", required = false) BigDecimal ogrzewanie,
            @RequestParam(name = "tankowanie", required = false) BigDecimal tankowanie){

        if(ogrzewanie == null) ogrzewanie = BigDecimal.ZERO;
        if(tankowanie == null) tankowanie = BigDecimal.ZERO;

        BigDecimal zuzycie = norma.multiply(normaVal).setScale(1, RoundingMode.HALF_UP);

        return new BigDecimal[]{
            before.subtract(zuzycie).subtract(ogrzewanie).add(tankowanie).setScale(1, RoundingMode.HALF_UP),
            zuzycie
        };

    }

}