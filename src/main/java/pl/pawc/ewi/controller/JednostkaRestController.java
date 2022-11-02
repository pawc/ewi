package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Jednostka;
import pl.pawc.ewi.repository.JednostkaRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class JednostkaRestController {

    private static final Logger logger = LogManager.getLogger(JednostkaRestController.class);
    private final JednostkaRepository jednostkaRepository;

    @PostMapping("jednostka")
    public void jednostka(
            @RequestBody Jednostka jednostka,
            HttpServletRequest request) {

        Jednostka j = jednostkaRepository.findById(jednostka.getId()).orElse(null);
        if (j == null) {
            j = new Jednostka();
        }
        j.setNazwa(jednostka.getNazwa());
        j.setWaga(jednostka.getWaga());

        jednostkaRepository.save(j);

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] POST /jednostka {}", ip, j);
    }

    @GetMapping("jednostkiGet")
    public List<Jednostka> jednostki(HttpServletRequest request){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        logger.info("[{}] /jednostki", ip);
        return jednostkaRepository.findAll();

    }

}