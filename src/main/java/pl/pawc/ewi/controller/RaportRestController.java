package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportMaszynaKilometry;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.service.RaportMaszynaKilometryService;
import pl.pawc.ewi.service.RaportService;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RaportRestController {

    private static final Logger logger = LogManager.getLogger(RaportRestController.class);

    private final RaportService raportService;
    private final RaportMaszynaKilometryService raportMaszynaKilometryService;

    @GetMapping("/raport")
    public List<Raport> raport(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/raport GET {}-{}", rok, miesiac);
        return raportService.getRaport(rok, miesiac, false);

    }

    @GetMapping("/getRaportKwartalny")
    public List<Raport> getRaportKwartalny(
            @RequestParam("rok") int rok,
            @RequestParam("kwartal") int kwartal){

        logger.info("/getRaportKwartalny GET {}-{}", rok, kwartal);
        return raportService.getRaport(rok, kwartal, true);

    }

    @GetMapping("/raportRoczny")
    public List<RaportRoczny> raportRoczny(
            @RequestParam("rok") int rok){

        logger.info("/raportRoczny GET {}", rok);
        return raportService.getRaportRoczny(rok);

    }

    @GetMapping("/getRaportMaszynaKilometry")
    public RaportMaszynaKilometry raportMaszynaKilometry(
            @RequestParam("start") String dateStart,
            @RequestParam("end") String dateEnd,
            @RequestParam("maszynaId") String maszynaId){


        try {
            logger.info("/raportMaszynaKilometry GET maszynaId: {} {}-{}", maszynaId, dateStart, dateEnd);
            return raportMaszynaKilometryService.getRaportKilometry(maszynaId, dateStart, dateEnd);
        } catch (ParseException e) {
            logger.warn("/raportMaszynaKilometry GET maszynaId: {} {}-{}", maszynaId, dateStart, dateEnd);
            throw new BadRequestException();
        }

    }

}