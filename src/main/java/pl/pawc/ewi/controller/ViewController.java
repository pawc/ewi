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
import pl.pawc.ewi.service.JednostkaService;
import pl.pawc.ewi.service.KategoriaService;
import pl.pawc.ewi.service.MaszynaService;

import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
@Controller
public class ViewController {

    private static final Logger logger = LogManager.getLogger(ViewController.class);
    public static final String MASZYNY = "maszyny";
    public static final String KATEGORIE = "kategorie";
    public static final String JEDNOSTKI = "jednostki";
    private final MaszynaService maszynaService;
    private final KategoriaService kategoriaService;
    private final JednostkaService jednostkaService;

    @RequestMapping("/")
    public String index(
            Model model){

        logger.info(" /");
        return "index";

    }

    @RequestMapping("/raportKwartalny")
    public String raportKwartalny(
            Model model){

        logger.info(" /raportKwartalny");
        return "raportKwartalny";

    }

    @RequestMapping("/raport2")
    public String raport2(
            Model model){

        logger.info(" /raport2");
        return "raportRoczny";

    }

    @RequestMapping("/raportMaszynaKilometry")
    public String raportMaszynaKilometry(
            Model model){

        model.addAttribute(MASZYNY, maszynaService.findAllActive());
        logger.info(" /raportMaszynaKilometry");
        return "raportMaszynaKilometry";

    }

    @RequestMapping("/dokumenty")
    public String dokumenty(
            Model model) {

        logger.info(" /dokumenty");
        model.addAttribute(MASZYNY, maszynaService.findAllActive());

        return "dokumenty";

    }

    @RequestMapping("/maszyny")
    public String maszynyView(
            Model model) {

        logger.info(" /maszyny");
        model.addAttribute(MASZYNY, maszynaService.findAll());
        model.addAttribute(KATEGORIE, kategoriaService.findAll());
        model.addAttribute(JEDNOSTKI, jednostkaService.findAll());

        return MASZYNY;

    }

    @RequestMapping("/jednostki")
    public String jednostkiView(
            Model model) {

        logger.info(" /jednostki");
        model.addAttribute(JEDNOSTKI, jednostkaService.findAll());

        return JEDNOSTKI;

    }

    @RequestMapping("/stany")
    public String stany(Model model) {

        logger.info(" /stany");
        return "stany";

    }

    @RequestMapping("/kilometry")
    public String kilometry(
            Model model) {

        logger.info(" /kilometry");
        return "kilometry";

    }

    @RequestMapping("/kategorie")
    public String kategorieView(
            Model model) {

        logger.info(" /kategorie");

        List<Kategoria> kategorie = Lists.newArrayList(kategoriaService.findAll());

        Iterable<Maszyna> allUncategorized = maszynaService.findAllUncategorized();
        Set<Maszyna> maszyny = Sets.newHashSet(allUncategorized);

        if(!maszyny.isEmpty()){
            Kategoria kategoria = new Kategoria();

            kategoria.setNazwa("Nieprzydzielone");
            kategoria.setMaszyny(maszyny);
            kategoria.setPrzenoszonaNaKolejnyOkres(false);

            kategorie.add(kategoria);
        }

        model.addAttribute(KATEGORIE, kategorie);

        return KATEGORIE;

    }

    @RequestMapping("/login")
    public String login() {

        logger.info(" /login");
        return "login";

    }

}