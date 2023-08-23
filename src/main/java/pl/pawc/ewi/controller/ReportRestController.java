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
public class ReportRestController {

    private static final Logger logger = LogManager.getLogger(ReportRestController.class);

    private final ReportService reportService;
    private final ReportMachineKilometersService reportMachineKilometersService;

    @GetMapping("/report")
    public List<Report> report(
            @RequestParam("year") int year,
            @RequestParam("month") int month){

        logger.info("/report GET {}-{}", year, month);
        return reportService.getReport(year, month, false);

    }

    @GetMapping("/getReportQuarterly")
    public List<Report> getReportQuarterly(
            @RequestParam("year") int year,
            @RequestParam("quarter") int quarter){

        logger.info("/getReportQuarterly GET {}-{}", year, quarter);
        return reportService.getReport(year, quarter, true);

    }

    @GetMapping("/reportAnnual")
    public List<AnnualReport> reportAnnual(
            @RequestParam("year") int year){

        logger.info("/reportAnnual GET {}", year);
        return reportService.getReportAnnual(year);

    }

    @GetMapping("/getReportMachineKilometers")
    public ReportMachineKilometers getReportMachineKilometers(
            @RequestParam("start") String dateStart,
            @RequestParam("end") String dateEnd,
            @RequestParam("machineId") String machineId){


        try {
            logger.info("/getReportMachineKilometers GET machineId: {} {}-{}", machineId, dateStart, dateEnd);
            return reportMachineKilometersService.getReportKilometers(machineId, dateStart, dateEnd);
        } catch (ParseException e) {
            logger.warn("/getReportMachineKilometers GET machineId: {} {}-{}", machineId, dateStart, dateEnd);
            throw new BadRequestException();
        }

    }

}