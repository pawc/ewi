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
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.service.MaszynaService;

@RequiredArgsConstructor
@RestController
public class MaszynaRestController {

    private final MaszynaService maszynaService;
    private static final Logger logger = LogManager.getLogger(MaszynaRestController.class);

    @GetMapping("/maszyna")
    public Maszyna maszynaGet(
            @RequestParam("id") String id,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        logger.info("/maszyna GET id={}", id);
        return maszynaService.get(id, miesiac);

    }

    @PostMapping(value = "/maszyna")
    public void maszynaPost(
            @RequestBody Maszyna maszyna) {

        if(maszynaService.findById(maszyna.getId()).isEmpty()){
            logger.info("/maszyna POST id={}", maszyna.getId());
            maszynaService.post(maszyna);
        }
        else{
            throw new BadRequestException("Maszyna " + maszyna.getId() + " already exists");
        }

    }

    @PutMapping(value = "/maszyna")
    public void maszynaPut(
            @RequestBody Maszyna maszyna) {

        if(maszynaService.findById(maszyna.getId()).isPresent()){
            logger.info("/maszyna PUT id={}", maszyna.getId());
            maszynaService.put(maszyna);
        }
        else{
            throw new BadRequestException("Maszyna " + maszyna.getId() + " does not exist");
        }

    }

}