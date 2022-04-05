package pl.pawc.ewi.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.repository.KategoriaRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
@Controller
public class ViewController {

    private static final Logger logger = LogManager.getLogger(ViewController.class);
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

        model.addAttribute("maszyny", maszynaRepository.findAllActive());
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

        List<Kategoria> kategorie = Lists.newArrayList(kategoriaRepository.findAll());

        Iterable<Maszyna> allUncategorized = maszynaRepository.findAllUncategorized();
        Set<Maszyna> maszyny = Sets.newHashSet(allUncategorized);

        Kategoria kategoria = new Kategoria();
        kategoria.setNazwa("Nieprzydzielone");
        kategoria.setMaszyny(maszyny);
        kategoria.setPrzenoszonaNaKolejnyOkres(false);

        kategorie.add(kategoria);

        model.addAttribute("kategorie", kategorie);

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