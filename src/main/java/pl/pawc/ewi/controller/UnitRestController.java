package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Unit;
import pl.pawc.ewi.service.UnitService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UnitRestController {

    private static final Logger logger = LogManager.getLogger(UnitRestController.class);
    private final UnitService unitService;

    @PostMapping("jednostka")
    public void jednostka(
            @RequestBody Unit unit) {

        logger.info("POST /jednostka {}", unit);
        unitService.put(unit);
    }

    @GetMapping("jednostkiGet")
    public List<Unit> jednostki(){

        logger.info("/jednostki");
        return unitService.findAll();

    }

}