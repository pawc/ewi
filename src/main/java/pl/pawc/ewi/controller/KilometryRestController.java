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

        List<Kilometry> by = kilometryRepository.findBy(kilometry.getMaszyna().getId(), kilometry.getRok(), kilometry.getMiesiac());

        if(by.isEmpty()){
            kilometryRepository.save(kilometry);
            logger.info("["+request.getRemoteAddr()+"] - /kilometry POST - dodano - " + kilometry);
        }
        else{
            Kilometry kilometryDB = by.get(0);
            kilometryDB.setWartosc(kilometry.getWartosc());
            kilometryRepository.save(kilometryDB);
            logger.info("["+request.getRemoteAddr()+"] - /kilometry POST - zaktualizowano - " + kilometry);
        }
    }

    @GetMapping("kilometryGet")
    public List<KilometryRaport> kilometryGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        return kilometryRepository.findBy(rok, miesiac);

    }

    @PostMapping("kilometryList")
    public void kilometryList(
            @RequestBody List<Kilometry> kilometry,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Kilometry> byParams;
        Kilometry kmDB;
        for(Kilometry km : kilometry){
            Optional<Maszyna> byId = maszynaRepository.findById(km.getMaszyna().getId());
            if(byId.isPresent()){
                if(!byId.get().isPrzenoszonaNaKolejnyOkres()) continue;
            }

            byParams = kilometryRepository.findBy(km.getMaszyna().getId(), km.getRok(), km.getMiesiac());
            if(byParams.isEmpty()){
                kilometryRepository.save(km);
                logger.info("["+request.getRemoteAddr()+"] - /kilometryList POST - dodano - " + km);
            }
            else{
                kmDB = byParams.get(0);
                kmDB.setWartosc(km.getWartosc());
                kilometryRepository.save(kmDB);
                logger.info("["+request.getRemoteAddr()+"] - /kilometryList POST - zaktualizowano - " + km);
            }
        }
    }

}