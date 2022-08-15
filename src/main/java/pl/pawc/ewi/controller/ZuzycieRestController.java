package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.service.UtilService;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class ZuzycieRestController {

    private final UtilService utilService;

    @RequestMapping("/calc")
    public BigDecimal[] calc(
            @RequestParam("before") BigDecimal before,
            @RequestParam("norma") BigDecimal norma,
            @RequestParam("normaVal") BigDecimal normaVal,
            @RequestParam(name = "ogrzewanie", required = false) BigDecimal ogrzewanie,
            @RequestParam(name = "tankowanie", required = false) BigDecimal tankowanie,
            @RequestParam(name = "czyZaokr1setna", required = false) boolean czyZaokr1setna){

        if(ogrzewanie == null) ogrzewanie = BigDecimal.ZERO;
        if(tankowanie == null) tankowanie = BigDecimal.ZERO;

        return utilService.calc(before, norma, normaVal, ogrzewanie, tankowanie, czyZaokr1setna);

    }

}