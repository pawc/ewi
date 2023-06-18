package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.service.DokumentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DokumentRestController {

    private static final Logger logger = LogManager.getLogger(DokumentRestController.class);
    private final DokumentService dokumentService;

    @RequestMapping("/dokument")
    public Dokument dokumentGet(
            @RequestParam("numer") String numer) {

        logger.info(" /dokument GET numer = {} ", numer);
        Dokument dokument = dokumentService.get(numer);
        return dokument == null ? new Dokument() : dokument;

    }

    @RequestMapping("/dokumentyGet")
    public List<Dokument> dokumentyGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info(" /dokumentyGet {}-{} ", rok, miesiac);
        List<Dokument> dokumenty = dokumentService.getDokumenty(rok, miesiac);
        dokumenty.forEach(d -> d.setZuzycie(null));
        return dokumenty;

    }

    @PostMapping("/dokument")
    public void dokumentPost(
            @RequestBody Dokument dokument) {

        logger.info(" /dokument POST numer={}", dokument.getNumer());
        dokumentService.post(dokument);

    }

    @PutMapping("/dokument")
    public void dokumentPut(
            @RequestBody Dokument dokument) {

        try {
            dokumentService.put(dokument);
            logger.info(" /dokument PUT numer={}", dokument.getNumer());
        }
        catch (DocumentNotFoundException e) {
            logger.warn(" /dokument PUT - DocumentNotFoundException");
            throw new BadRequestException();
        }

    }

    @DeleteMapping("/dokument")
    public void dokumentDelete(
            @RequestParam("numer") String numer) {

        logger.info(" /dokument DELETE numer={}", numer);
        dokumentService.delete(numer);


    }

}