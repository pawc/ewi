package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportMaszynaKilometry;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.service.RaportMaszynaKilometryService;
import pl.pawc.ewi.service.RaportService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RaportRestController {

    private static final Logger logger = LogManager.getLogger(RaportRestController.class);

    private final RaportService raportService;
    private final RaportMaszynaKilometryService raportMaszynaKilometryService;

    @RequestMapping(value = "/raport", method = RequestMethod.GET)
    public List<Raport> raport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /raport GET {}-{}", ip, rok, miesiac);
        return raportService.getRaport(rok, miesiac, false);

    }

    @RequestMapping(value = "/getRaportKwartalny", method = RequestMethod.GET)
    public List<Raport> getRaportKwartalny(
            HttpServletRequest request,
            @RequestParam("rok") int rok,
            @RequestParam("kwartal") int kwartal){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /getRaportKwartalny GET {}-{}", ip, rok, kwartal);
        return raportService.getRaport(rok, kwartal, true);

    }

    @RequestMapping(value = "/raportRoczny", method = RequestMethod.GET)
    public List<RaportRoczny> raportRoczny(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /raportRoczny GET {}", ip, rok);
        return raportService.getRaportRoczny(rok);

    }

    @RequestMapping(value = "/getRaportMaszynaKilometry", method = RequestMethod.GET)
    public RaportMaszynaKilometry raportMaszynaKilometry(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("start") String dateStart,
            @RequestParam("end") String dateEnd,
            @RequestParam("maszynaId") String maszynaId){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();

        try {
            logger.info("[{}] /raportMaszynaKilometry GET maszynaId: {} {}-{}", ip, maszynaId, dateStart, dateEnd);
            return raportMaszynaKilometryService.getRaportKilometry(maszynaId, dateStart, dateEnd);
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("[{}] /raportMaszynaKilometry GET maszynaId: {} {}-{}", ip, maszynaId, dateStart, dateEnd);
            return null;
        }

    }

}