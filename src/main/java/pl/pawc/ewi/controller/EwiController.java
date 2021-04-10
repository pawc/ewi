package pl.pawc.ewi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class EwiController {

    private static final Logger logger = Logger.getLogger(EwiController.class);

    @Autowired
    DokumentRepository dokumentRepository;

    @Autowired
    MaszynaRepository maszynaRepository;

    @RequestMapping("/")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response){

        logger.info("["+request.getRemoteAddr()+"] - / " );
        return "index";

    }

    @RequestMapping("/dokumenty")
    public String dokumenty(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        model.addAttribute("dokumenty", dokumentRepository.findAll());
        model.addAttribute("maszyny", maszynaRepository.findAll());
        logger.info("["+request.getRemoteAddr()+"] - /dokumenty" );

        return "dokumenty";

    }

    @RequestMapping("/maszyny")
    public String maszyny(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        model.addAttribute("maszyny", maszynaRepository.findAll());
        logger.info("["+request.getRemoteAddr()+"] - /maszyny" );

        return "maszyny";

    }

    @RequestMapping("/login")
    public String login(
            HttpServletRequest request,
            HttpServletResponse response) {

        logger.info("["+request.getRemoteAddr()+"] - strona logowania" );
        return "login";

    }

}