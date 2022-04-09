package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.StanRaport;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StanRestController {

    private static final Logger logger = LogManager.getLogger(StanRestController.class);
    private final StanRepository stanRepository;
    private final NormaRepository normaRepository;

    @PostMapping("stan")
    public void stanPost(
            @RequestBody Stan stan,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        List<Stan> byParams = stanRepository.findBy(stan.getNorma(), stan.getRok(), stan.getMiesiac());
        if(byParams.isEmpty()){
            logger.info("[{}] /stan POST dodano {} - normaID={}", ip, stan, stan.getNorma().getId());
            stanRepository.save(stan);
        }
        else{
            logger.info("[{}] /stan POST zaktualizowano {} - normaID={}", ip, stan, stan.getNorma().getId());
            Stan stanDB = byParams.get(0);
            stanDB.setWartosc(stan.getWartosc());
            stanRepository.save(stanDB);
        }
    }

    @PostMapping("stany")
    public void stanyPost(
            @RequestBody List<Stan> stany,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Stan> byParams;
        Stan stanDB;
        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        for(Stan stan : stany){

            Optional<Norma> byId = normaRepository.findById(stan.getNorma().getId());
            if(byId.isPresent()){
                if(!byId.get().getMaszyna().isPrzenoszonaNaKolejnyOkres()) continue;
            }

            byParams = stanRepository.findBy(stan.getNorma(), stan.getRok(), stan.getMiesiac());
            if(byParams.isEmpty()){
                logger.info("[{}] /stany POST dodano {} - normaID={}", ip, stan, stan.getNorma().getId());
                stanRepository.save(stan);
            }
            else{
                logger.info("[{}] /stany POST zaktualizowano {} - normaID={}", ip, stan, stan.getNorma().getId());
                stanDB = byParams.get(0);
                stanDB.setWartosc(stan.getWartosc());
                stanRepository.save(stanDB);
            }
        }
    }

    @GetMapping("stanyGet")
    public List<StanRaport> stanyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /stanyGet {}-{}", ip, rok, miesiac);
        return stanRepository.findBy(rok, miesiac);

    }

    @PutMapping("stan")
    public void stanPut(
            @RequestBody Stan stan,
            HttpServletRequest request,
            HttpServletResponse response){

        List<Stan> byParams;
        Stan stanDB;
        byParams = stanRepository.findBy(stan.getNorma(), stan.getRok(), stan.getMiesiac());
        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        if(byParams.isEmpty()){
            logger.info("[{}] /stan PUT dodano {} - normaID={}", ip, stan, stan.getNorma().getId());
            stanRepository.save(stan);
        }
        else{
            logger.info("[{}] /stan PUT zaktualizowano {} - normaID={}", ip, stan, stan.getNorma().getId());
            stanDB = byParams.get(0);
            stanDB.setWartosc(stan.getWartosc());
            stanRepository.save(stanDB);
        }

    }

}