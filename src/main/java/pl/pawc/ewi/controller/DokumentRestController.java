package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;
import pl.pawc.ewi.service.DokumentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DokumentRestController {

    private static final Logger logger = LogManager.getLogger(DokumentRestController.class);
    private final DokumentRepository dokumentRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final MaszynaRepository maszynaRepository;
    private final DokumentService dokumentService;
    private final StanRepository stanRepository;
    private final KilometryRepository kilometryRepository;

    @RequestMapping("/dokument")
    public Dokument dokumentGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("numer") String numer,
            @RequestParam(name = "miesiac", required = false) String miesiac) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        Dokument dokument =  dokumentService.get(numer);

        if(dokument != null) {
            logger.info("[{}] /dokument GET numer = {} ", ip, numer);
        }
        else {
            dokument = new Dokument();
            logger.warn("[{}] /dokument GET numer = {} - nie odnaleziono ", ip, numer);
        }
        return dokument;

    }

    @RequestMapping("/dokumentyGet")
    public List<Dokument> dokumentyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /dokumentyGet {}-{} ", ip, rok, miesiac);
        List<Dokument> dokumenty = dokumentRepository.getDokumenty(rok, miesiac);
        dokumenty.forEach(d -> d.setZuzycie(null));
        return dokumenty;

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.POST)
    public void dokumentPost(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        dokumentService.post(dokument);

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /dokument POST numer={}", ip, dokument.getNumer());

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.PUT)
    public void dokumentPut(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();

        try {
            dokumentService.put(dokument);
            logger.info("[{}] /dokument PUT numer={}", ip, dokument.getNumer());
        } catch (DocumentNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("[{}] /dokument PUT - DocumentNotFoundException", ip);
        }

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.DELETE)
    public void dokumentDelete(
            @RequestParam("numer") String numer,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();

        try {
            dokumentService.delete(numer);
            logger.info("[{}] /dokument DELETE numer={}", ip, numer);
        } catch (DocumentNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("[{}] /dokument DELETE numer={} DocumentNotFoundException", ip, numer);
        }

    }

}