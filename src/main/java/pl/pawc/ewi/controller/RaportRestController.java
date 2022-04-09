package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.repository.RaportRepository;
import pl.pawc.ewi.repository.RaportRocznyRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RaportRestController {

    private static final Logger logger = LogManager.getLogger(RaportRestController.class);

    private final RaportRepository raportRepository;
    private final RaportRocznyRepository raportRocznyRepository;

    @RequestMapping(value = "/raport", method = RequestMethod.GET)
    public List<Raport> raport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /raport GET {}-{}", ip, rok, miesiac);
        return raportRepository.getRaport(rok, miesiac);

    }

    @RequestMapping(value = "/raportRoczny", method = RequestMethod.GET)
    public List<RaportRoczny> raportRoczny(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /raportRoczny GET {}", ip, rok);
        return raportRocznyRepository.getRaport(rok);

    }

}