package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.model.KilometersReport;
import pl.pawc.ewi.service.KilometersService;
import pl.pawc.ewi.service.ReportService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class KilometersRestController {

    private static final Logger logger = LogManager.getLogger(KilometersRestController.class);

    private final KilometersService kilometersService;
    private final ReportService reportService;

    @PostMapping("kilometry")
    public void kilometry(
            @RequestBody Kilometers kilometers) {

        if(kilometersService.post(kilometers)) logger.info("/kilometry POST dodano {}", kilometers);
        else logger.info("/kilometry POST zaktualizowano {}", kilometers);

    }

    @GetMapping("kilometryGet")
    public List<KilometersReport> kilometryGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/kilometryGet {}-{}", rok, miesiac);
        return reportService.getKilometryRaport(rok, miesiac);

    }

    @PostMapping("kilometryList")
    public void kilometryList(
            @RequestBody List<Kilometers> kilometers) {

        kilometersService.post(kilometers);
    }

}