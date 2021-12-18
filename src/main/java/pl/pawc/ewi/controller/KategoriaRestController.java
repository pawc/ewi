package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.repository.KategoriaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class KategoriaRestController {

    private static final Logger logger = LogManager.getLogger(KategoriaRestController.class);
    private final KategoriaRepository kategoriaRepository;

    @PostMapping("/kategoria")
    public void kategoriaPost(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Kategoria kategoria){

        Optional<Kategoria> byId = kategoriaRepository.findById(kategoria.getNazwa());
        if(!byId.isPresent()){
            kategoriaRepository.save(kategoria);
        }
    }

    @DeleteMapping("/kategoria")
    public void kategoriaDelete(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Kategoria kategoria){

        Optional<Kategoria> byId = kategoriaRepository.findById(kategoria.getNazwa());
        if(byId.isPresent()){
            kategoriaRepository.delete(kategoria);
        }
    }

    @PutMapping("/togglePrzenoszonaNaKolejnyOkres")
    public void togglePrzenoszonaNaKolejnyOkres(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Kategoria kategoria) {

        Optional<Kategoria> byId = kategoriaRepository.findById(kategoria.getNazwa());
        if(byId.isPresent()){
            Kategoria kat = byId.get();
            kat.setPrzenoszonaNaKolejnyOkres(!kat.isPrzenoszonaNaKolejnyOkres());
            kategoriaRepository.save(kat);
        }

    }

}