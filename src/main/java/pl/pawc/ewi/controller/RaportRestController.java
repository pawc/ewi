package pl.pawc.ewi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.repository.RaportRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class RaportRestController {

    private static final Logger logger = Logger.getLogger(RaportRestController.class);

    @Autowired
    RaportRepository raportRepository;

    @RequestMapping(value = "/raport", method = RequestMethod.GET)
    public List<Raport> raport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        List<Raport> raport = raportRepository.getRaport(rok, miesiac);
        logger.info("["+request.getRemoteAddr()+"] - /raport GET " + rok + "-" + miesiac);

        return raport;

    }

}