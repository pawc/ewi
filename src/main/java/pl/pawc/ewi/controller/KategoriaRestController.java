package pl.pawc.ewi.controller;

import com.sun.istack.NotNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.repository.KategoriaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class KategoriaRestController {

    private static final Logger logger = Logger.getLogger(KategoriaRestController.class);

    @Autowired
    KategoriaRepository kategoriaRepository;

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

}