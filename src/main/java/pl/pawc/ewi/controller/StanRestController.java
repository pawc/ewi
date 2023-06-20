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
import pl.pawc.ewi.service.StanService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StanRestController {

    private static final Logger logger = LogManager.getLogger(StanRestController.class);
    private final StanService stanService;

    @PutMapping("stan")
    public void stanPut(
            @RequestBody Stan stan) {

        if(stanService.post(stan)) logger.info("/stan dodano {} - normaID={}", stan, stan.getNorma().getId());
        else logger.info("/stan zaktualizowano {} - normaID={}", stan, stan.getNorma().getId());
    }

    @PostMapping("stany")
    public void stanyPost(
            @RequestBody List<Stan> stany) {

        stanService.stanyPost(stany);
        for(Stan stan : stany){
            logger.info("/stany {}-{} - normaID={}", stan.getRok(), stan.getMiesiac(), stan.getNorma().getId());
        }
    }

    @GetMapping("stanyGet")
    public List<RaportStan> stanyGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/stanyGet {}-{}", rok, miesiac);
        return stanService.findBy(rok, miesiac);

    }

}