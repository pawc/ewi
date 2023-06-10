package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.service.MaszynaService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class MaszynaRestController {

    private final MaszynaService maszynaService;
    private static final Logger logger = LogManager.getLogger(MaszynaRestController.class);

    @RequestMapping("/maszyna")
    public Maszyna maszynaGet(
            @RequestParam("id") String id,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        logger.info(" /maszyna GET id={}", id);
        return maszynaService.get(id, miesiac);

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.POST)
    public void maszynaPost(
            @RequestBody Maszyna maszyna) {

        if(maszynaService.post(maszyna) != null){
            logger.info(" /maszyna POST id={}", maszyna.getId());
        }
        else{
            logger.warn(" /maszyna POST id={} BAD REQUEST", maszyna.getId());
            throw new BadRequestException();
        }

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.PUT)
    public void maszynaPut(
            @RequestBody Maszyna maszyna) {

        Optional<Maszyna> byId = maszynaService.findById(maszyna.getId());

        if(byId.isPresent()){
            logger.info(" /maszyna PUT id={}", maszyna.getId());
            maszynaService.put(maszyna);
        }
        else{
            logger.warn(" /maszyna PUT id={} - BAD REQUEST", maszyna.getId());
            throw new BadRequestException();
        }

    }

}