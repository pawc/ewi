package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.service.KilometryService;
import pl.pawc.ewi.service.RaportService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class KilometryRestController {

    private static final Logger logger = LogManager.getLogger(KilometryRestController.class);

    private final KilometryService kilometryService;
    private final RaportService raportService;

    @PostMapping("kilometry")
    public void kilometry(
            @RequestBody Kilometry kilometry) {

        if(kilometryService.post(kilometry)) logger.info(" /kilometry POST dodano {}", kilometry);
        else logger.info(" /kilometry POST zaktualizowano {}", kilometry);

    }

    @GetMapping("kilometryGet")
    public List<RaportKilometry> kilometryGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info(" /kilometryGet {}-{}", rok, miesiac);
        return raportService.getKilometryRaport(rok, miesiac);

    }

    @PostMapping("kilometryList")
    public void kilometryList(
            @RequestBody List<Kilometry> kilometry) {

        kilometryService.post(kilometry);
    }

}