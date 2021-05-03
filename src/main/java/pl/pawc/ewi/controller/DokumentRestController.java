package pl.pawc.ewi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class DokumentRestController {

    private static final Logger logger = Logger.getLogger(DokumentRestController.class);

    @Autowired
    DokumentRepository dokumentRepository;

    @Autowired
    ZuzycieRepository zuzycieRepository;

    @Autowired
    MaszynaRepository maszynaRepository;

    @Autowired
    NormaRepository normaRepository;

    @RequestMapping("/dokument")
    public Dokument dokumentGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("numer") String numer){

        Optional<Dokument> result = dokumentRepository.findById(numer);
        Dokument dokument = null;
        if(result.isPresent()){
            dokument = result.get();
            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokumentId(dokument.getNumer());
            for(Zuzycie zuzycie : zuzycieList){
                zuzycie.setDokument(null);
                zuzycie.getNorma().setMaszyna(null);
            }
            dokument.setZuzycie(zuzycieList);
            logger.info("["+request.getRemoteAddr()+"] - /dokument GET numer="+numer);
        }
        else{
            dokument = new Dokument();
            logger.warn("["+request.getRemoteAddr()+"] - /dokument GET numer="+numer + ". Nie odnaleziono");
        }
        return dokument;

    }

    @RequestMapping("/dokumentyGet")
    public List<Dokument> dokumentyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        return dokumentRepository.getDokumenty(rok, miesiac);

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.POST)
    public void dokumentPost(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Zuzycie> zuzycia = dokument.getZuzycie();
        for(Zuzycie zuzycie : zuzycia){
            Norma norma = normaRepository.findById(zuzycie.getNorma().getId()).get();
            zuzycie.setNorma(norma);
        }

        Maszyna maszyna = maszynaRepository.findById(dokument.getMaszyna().getId()).get();
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        for(Zuzycie zuzycie : zuzycia){
            zuzycie.setDokument(dokument);
            zuzycieRepository.save(zuzycie);
        }

        logger.info("["+request.getRemoteAddr()+"] - /dokument POST numer="+dokument.getNumer());

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.PUT)
    public void dokumentPut(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Dokument> byId = dokumentRepository.findById(dokument.getNumer());

        if(byId.isPresent()){
            Dokument dokumentDB = byId.get();

            dokumentDB.setData(dokument.getData());
            dokumentDB.setKilometry(dokument.getKilometry());
            dokumentRepository.save(dokumentDB);

            for(Zuzycie zuzycie : dokument.getZuzycie()) {
                Zuzycie zuzycieDB = zuzycieRepository.findById(zuzycie.getId()).get();
                if (zuzycieDB.getWartosc() != zuzycie.getWartosc() || zuzycieDB.getZatankowano() != zuzycie.getZatankowano()) {
                    zuzycieDB.setWartosc(zuzycie.getWartosc());
                    zuzycieDB.setZatankowano(zuzycie.getZatankowano());
                    zuzycieRepository.save(zuzycieDB);
                }
            }

            logger.info("["+request.getRemoteAddr()+"] - /dokument PUT numer="+dokument.getNumer());

        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("["+request.getRemoteAddr()+"] - /dokument PUT - BAD REQUEST");
        }

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.DELETE)
    public void dokumentDelete(
            @RequestParam("numer") String numer,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Dokument> byId = dokumentRepository.findById(numer);

        if(byId.isPresent()) {
            Dokument dokumentDB = byId.get();

            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokumentId(dokumentDB.getNumer());
            zuzycieRepository.deleteAll(zuzycieList);
            dokumentRepository.delete(dokumentDB);

        }

        logger.info("["+request.getRemoteAddr()+"] - /dokument DELETE numer="+numer);

    }

}