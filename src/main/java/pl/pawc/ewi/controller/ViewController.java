package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pawc.ewi.repository.KategoriaRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class ViewController {

    private static final Logger logger = Logger.getLogger(ViewController.class);
    private final MaszynaRepository maszynaRepository;
    private final KategoriaRepository kategoriaRepository;

    @RequestMapping("/")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response){

        logger.info("["+request.getRemoteAddr()+"] - / " );
        return "index";

    }

    @RequestMapping("/raport2")
    public String raport2(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response){

        logger.info("["+request.getRemoteAddr()+"] - /raport2 " );
        return "raportRoczny";

    }

    @RequestMapping("/dokumenty")
    public String dokumenty(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

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
        model.addAttribute("kategorie", kategoriaRepository.findAll());
        logger.info("["+request.getRemoteAddr()+"] - /maszyny" );

        return "maszyny";

    }

    @RequestMapping("/stany")
    public String stany(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        return "stany";

    }

    @RequestMapping("/kilometry")
    public String kilometry(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        return "kilometry";

    }

    @RequestMapping("/kategorie")
    public String kategorie(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        model.addAttribute("kategorie", kategoriaRepository.findAll());
        return "kategorie";

    }

    @RequestMapping("/login")
    public String login(
            HttpServletRequest request,
            HttpServletResponse response) {

        logger.info("["+request.getRemoteAddr()+"] - strona logowania" );
        return "login";

    }

}