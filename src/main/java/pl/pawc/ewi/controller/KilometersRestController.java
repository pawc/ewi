package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.model.KilometersReport;
import pl.pawc.ewi.service.KilometersService;
import pl.pawc.ewi.service.ReportService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class KilometersRestController {

    private static final Logger logger = LogManager.getLogger(KilometersRestController.class);

    private final KilometersService kilometersService;
    private final ReportService reportService;

    @PostMapping("kilometers")
    public void kilometersPost(
            @RequestBody Kilometers kilometers) {

        if(kilometersService.post(kilometers)) logger.info("/kilometers POST added {}", kilometers);
        else logger.info("/kilometers POST updated {}", kilometers);

    }

    @GetMapping("kilometers")
    public List<KilometersReport> kilometryGet(
            @RequestParam("year") int year,
            @RequestParam("month") int month){

        logger.info("/KilometersReport {}-{}", year, month);
        return reportService.getKilometersReport(year, month);

    }

    @PostMapping("kilometersList")
    public void kilometersList(
            @RequestBody List<Kilometers> kilometers) {

        kilometersService.post(kilometers);
    }

}