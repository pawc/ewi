package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.service.KategoriaService;

@RequiredArgsConstructor
@RestController
public class KategoriaRestController {

    private static final Logger logger = LogManager.getLogger(KategoriaRestController.class);
    private final KategoriaService kategoriaService;

    @PostMapping("/kategoria")
    public void post(
            @RequestBody Kategoria kategoria){

        if(kategoriaService.post(kategoria)) logger.info("/kategoria POST {}", kategoria.getNazwa());
        else {
            logger.warn("/kategoria POST {} - category already exists", kategoria.getNazwa());
            throw new BadRequestException();
        }

    }

    @DeleteMapping("/kategoria")
    public void delete(
            @RequestBody Kategoria kategoria){

        logger.info("/kategoria DELETE {}", kategoria.getNazwa());
        kategoriaService.delete(kategoria);

    }

    @PutMapping("/togglePrzenoszonaNaKolejnyOkres")
    public void toggle(
            @RequestBody Kategoria kategoria) {

        logger.info("/togglePrzenoszonaNaKolejnyOkres PUT {}", kategoria.getNazwa());
        if(!kategoriaService.togglePrzenoszonaNaKolejnyOkres(kategoria)) throw new BadRequestException();

    }

}