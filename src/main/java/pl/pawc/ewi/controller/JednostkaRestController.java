package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Jednostka;
import pl.pawc.ewi.service.JednostkaService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class JednostkaRestController {

    private static final Logger logger = LogManager.getLogger(JednostkaRestController.class);
    private final JednostkaService jednostkaService;

    @PostMapping("jednostka")
    public void jednostka(
            @RequestBody Jednostka jednostka) {

        logger.info("POST /jednostka {}", jednostka);
        jednostkaService.put(jednostka);
    }

    @GetMapping("jednostkiGet")
    public List<Jednostka> jednostki(){

        logger.info("/jednostki");
        return jednostkaService.findAll();

    }

}