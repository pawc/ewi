package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.model.KilometryRaport;
import pl.pawc.ewi.repository.KilometryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class KilometryRestController {

    private static final Logger logger = Logger.getLogger(KilometryRestController.class);
    private final KilometryRepository kilometryRepository;

    @PostMapping("kilometry")
    public void kilometry(
            @RequestBody Kilometry kilometry,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Kilometry> by = kilometryRepository.findBy(kilometry.getMaszyna().getId(), kilometry.getRok(), kilometry.getMiesiac());

        if(by.isEmpty()){
            kilometryRepository.save(kilometry);
            logger.info("["+request.getRemoteAddr()+"] - /kilometry POST - dodano - " + kilometry.toString());
        }
        else{
            Kilometry kilometryDB = by.get(0);
            kilometryDB.setWartosc(kilometry.getWartosc());
            kilometryRepository.save(kilometryDB);
            logger.info("["+request.getRemoteAddr()+"] - /kilometry POST - zaktualizowano - " + kilometry.toString());
        }
    }

    @GetMapping("kilometryGet")
    public List<KilometryRaport> kilometryGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        Iterable<KilometryRaport> all = kilometryRepository.findBy(rok, miesiac);
        return (List<KilometryRaport>) all;

    }

    @PostMapping("kilometryList")
    public void kilometryList(
            @RequestBody List<Kilometry> kilometry,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Kilometry> byParams = null;
        Kilometry kmDB = null;
        for(Kilometry km : kilometry){
            byParams = kilometryRepository.findBy(km.getMaszyna().getId(), km.getRok(), km.getMiesiac());
            if(byParams.isEmpty()){
                kilometryRepository.save(km);
                logger.info("["+request.getRemoteAddr()+"] - /kilometryList POST - dodano - " + km.toString());
            }
            else{
                kmDB = byParams.get(0);
                kmDB.setWartosc(km.getWartosc());
                kilometryRepository.save(kmDB);
                logger.info("["+request.getRemoteAddr()+"] - /kilometryList POST - zaktualizowano - " + km.toString());
            }
        }
    }

}