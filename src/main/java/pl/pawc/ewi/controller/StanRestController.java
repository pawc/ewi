package pl.pawc.ewi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.StanRaport;
import pl.pawc.ewi.repository.StanRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class StanRestController {

    private static final Logger logger = Logger.getLogger(StanRestController.class);

    @Autowired
    StanRepository stanRepository;

    @PostMapping("stan")
    public void stanPost(
            @RequestBody Stan stan,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Stan> byParams = stanRepository.findBy(stan.getNorma().getId(), stan.getRok(), stan.getMiesiac());
        if(byParams.isEmpty()){
            stanRepository.save(stan);
            logger.info("["+request.getRemoteAddr()+"] - /stan POST - dodano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
        }
        else{
            Stan stanDB = byParams.get(0);
            stanDB.setWartosc(stan.getWartosc());
            stanRepository.save(stanDB);
            logger.info("["+request.getRemoteAddr()+"] - /stan POST - zaktualizowano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
        }
    }

    @PostMapping("stany")
    public void stanyPost(
            @RequestBody List<Stan> stany,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Stan> byParams = null;
        Stan stanDB = null;
        for(Stan stan : stany){
            byParams = stanRepository.findBy(stan.getNorma().getId(), stan.getRok(), stan.getMiesiac());
            if(byParams.isEmpty()){
                stanRepository.save(stan);
                logger.info("["+request.getRemoteAddr()+"] - /stany POST - dodano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
            }
            else{
                stanDB = byParams.get(0);
                stanDB.setWartosc(stan.getWartosc());
                stanRepository.save(stanDB);
                logger.info("["+request.getRemoteAddr()+"] - /stany POST - zaktualizowano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
            }
        }
    }

    @GetMapping("stanyGet")
    public List<StanRaport> stanyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        Iterable<StanRaport> all = stanRepository.findBy(rok, miesiac);
        return (List<StanRaport>) all;

    }

    @PutMapping("stan")
    public void stanPut(
            @RequestBody Stan stan,
            HttpServletRequest request,
            HttpServletResponse response){

        List<Stan> byParams = null;
        Stan stanDB = null;
        byParams = stanRepository.findBy(stan.getNorma().getId(), stan.getRok(), stan.getMiesiac());
        if(byParams.isEmpty()){
            stanRepository.save(stan);
            logger.info("["+request.getRemoteAddr()+"] - /stan PUT - dodano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
        }
        else{
            stanDB = byParams.get(0);
            stanDB.setWartosc(stan.getWartosc());
            stanRepository.save(stanDB);
            logger.info("["+request.getRemoteAddr()+"] - /stan PUT - zaktualizowano - " + stan.toString() + " - normaID=" + stan.getNorma().getId());
        }

    }

}