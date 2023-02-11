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
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.RaportStan;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.service.StanService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StanRestController {

    private static final Logger logger = LogManager.getLogger(StanRestController.class);
    private final StanRepository stanRepository;
    private final NormaRepository normaRepository;
    private final StanService stanService;

    @PutMapping("stan")
    public void stanPut(
            @RequestBody Stan stan,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        if(stanService.post(stan)){
            logger.info("[{}] /stan dodano {} - normaID={}", ip, stan, stan.getNorma().getId());
        }
        else{
            logger.info("[{}] /stan zaktualizowano {} - normaID={}", ip, stan, stan.getNorma().getId());
        }
    }

    @PostMapping("stany")
    public void stanyPost(
            @RequestBody List<Stan> stany,
            HttpServletRequest request,
            HttpServletResponse response) {

        stanService.stanyPost(stany);
        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        for(Stan stan : stany){
            logger.info("[{}] /stany {}-{} - normaID={}", ip, stan.getRok(), stan.getMiesiac(), stan.getNorma().getId());
        }
    }

    @GetMapping("stanyGet")
    public List<RaportStan> stanyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /stanyGet {}-{}", ip, rok, miesiac);
        return stanService.findBy(rok, miesiac);

    }

}