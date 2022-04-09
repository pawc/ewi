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
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.KilometryRaport;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class KilometryRestController {

    private static final Logger logger = LogManager.getLogger(KilometryRestController.class);

    private final KilometryRepository kilometryRepository;
    private final MaszynaRepository maszynaRepository;

    @PostMapping("kilometry")
    public void kilometry(
            @RequestBody Kilometry kilometry,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Kilometry> by = kilometryRepository.findBy(kilometry.getMaszyna(), kilometry.getRok(), kilometry.getMiesiac());
        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();

        if(by.isEmpty()){
            logger.info("[{}] /kilometry POST dodano {}", ip, kilometry);
            kilometryRepository.save(kilometry);
        }
        else{
            logger.info("[{}] /kilometry POST zaktualizowano {}", ip, kilometry);
            Kilometry kilometryDB = by.get(0);
            kilometryDB.setWartosc(kilometry.getWartosc());
            kilometryRepository.save(kilometryDB);
        }
    }

    @GetMapping("kilometryGet")
    public List<KilometryRaport> kilometryGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /kilometryGet {}-{}", ip, rok, miesiac);
        return kilometryRepository.findBy(rok, miesiac);

    }

    @PostMapping("kilometryList")
    public void kilometryList(
            @RequestBody List<Kilometry> kilometry,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Kilometry> byParams;
        Kilometry kmDB;
        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        for(Kilometry km : kilometry){
            Optional<Maszyna> byId = maszynaRepository.findById(km.getMaszyna().getId());
            if(byId.isPresent()){
                if(!byId.get().isPrzenoszonaNaKolejnyOkres()) continue;
            }

            byParams = kilometryRepository.findBy(km.getMaszyna(), km.getRok(), km.getMiesiac());
            if(byParams.isEmpty()){
                logger.info("[{}] /kilometryList POST dodano {}", ip, km);
                kilometryRepository.save(km);
            }
            else{
                logger.info("[{}] /kilometryList POST zaktualizowano {}", ip, km);
                kmDB = byParams.get(0);
                kmDB.setWartosc(km.getWartosc());
                kilometryRepository.save(kmDB);
            }
        }
    }

}