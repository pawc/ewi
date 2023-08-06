package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.model.Report;
import pl.pawc.ewi.model.ReportMachineKilometers;
import pl.pawc.ewi.model.AnnualReport;
import pl.pawc.ewi.service.ReportMachineKilometersService;
import pl.pawc.ewi.service.ReportService;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RaportRestController {

    private static final Logger logger = LogManager.getLogger(RaportRestController.class);

    private final ReportService reportService;
    private final ReportMachineKilometersService reportMachineKilometersService;

    @GetMapping("/raport")
    public List<Report> raport(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/raport GET {}-{}", rok, miesiac);
        return reportService.getRaport(rok, miesiac, false);

    }

    @GetMapping("/getRaportKwartalny")
    public List<Report> getRaportKwartalny(
            @RequestParam("rok") int rok,
            @RequestParam("kwartal") int kwartal){

        logger.info("/getRaportKwartalny GET {}-{}", rok, kwartal);
        return reportService.getRaport(rok, kwartal, true);

    }

    @GetMapping("/raportRoczny")
    public List<AnnualReport> raportRoczny(
            @RequestParam("rok") int rok){

        logger.info("/raportRoczny GET {}", rok);
        return reportService.getRaportRoczny(rok);

    }

    @GetMapping("/getRaportMaszynaKilometry")
    public ReportMachineKilometers raportMaszynaKilometry(
            @RequestParam("start") String dateStart,
            @RequestParam("end") String dateEnd,
            @RequestParam("maszynaId") String maszynaId){


        try {
            logger.info("/raportMaszynaKilometry GET maszynaId: {} {}-{}", maszynaId, dateStart, dateEnd);
            return reportMachineKilometersService.getRaportKilometry(maszynaId, dateStart, dateEnd);
        } catch (ParseException e) {
            logger.warn("/raportMaszynaKilometry GET maszynaId: {} {}-{}", maszynaId, dateStart, dateEnd);
            throw new BadRequestException();
        }

    }

}