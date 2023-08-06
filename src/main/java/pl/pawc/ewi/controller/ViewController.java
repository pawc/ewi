package pl.pawc.ewi.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.service.UnitService;
import pl.pawc.ewi.service.CategoryService;
import pl.pawc.ewi.service.MachineService;

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
    private final MachineService machineService;
    private final CategoryService categoryService;
    private final UnitService unitService;

    @RequestMapping("/")
    public String index(
            Model model){

        logger.info("/");
        return "index";

    }

    @RequestMapping("/raportKwartalny")
    public String raportKwartalny(
            Model model){

        logger.info("/raportKwartalny");
        return "raportKwartalny";

    }

    @RequestMapping("/raport2")
    public String raport2(
            Model model){

        logger.info("/raport2");
        return "raportRoczny";

    }

    @RequestMapping("/raportMaszynaKilometry")
    public String raportMaszynaKilometry(
            Model model){

        model.addAttribute(MASZYNY, machineService.findAllActive());
        logger.info("/raportMaszynaKilometry");
        return "raportMaszynaKilometry";

    }

    @RequestMapping("/dokumenty")
    public String dokumenty(
            Model model) {

        logger.info("/dokumenty");
        model.addAttribute(MASZYNY, machineService.findAllActive());

        return "dokumenty";

    }

    @RequestMapping("/maszyny")
    public String maszynyView(
            Model model) {

        logger.info("/maszyny");
        model.addAttribute(MASZYNY, machineService.findAll());
        model.addAttribute(KATEGORIE, categoryService.findAll());
        model.addAttribute(JEDNOSTKI, unitService.findAll());

        return MASZYNY;

    }

    @RequestMapping("/jednostki")
    public String jednostkiView(
            Model model) {

        logger.info("/jednostki");
        model.addAttribute(JEDNOSTKI, unitService.findAll());

        return JEDNOSTKI;

    }

    @RequestMapping("/stany")
    public String stany(Model model) {

        logger.info("/stany");
        return "stany";

    }

    @RequestMapping("/kilometry")
    public String kilometry(
            Model model) {

        logger.info("/kilometry");
        return "kilometry";

    }

    @RequestMapping("/kategorie")
    public String kategorieView(
            Model model) {

        logger.info("/kategorie");

        List<Category> kategorie = Lists.newArrayList(categoryService.findAll());

        Iterable<Machine> allUncategorized = machineService.findAllUncategorized();
        Set<Machine> maszyny = Sets.newHashSet(allUncategorized);

        if(!maszyny.isEmpty()){
            Category category = new Category();

            category.setName("Nieprzydzielone");
            category.setMachines(maszyny);
            category.setCarriedOver(false);

            kategorie.add(category);
        }

        model.addAttribute(KATEGORIE, kategorie);

        return KATEGORIE;

    }

    @RequestMapping("/login")
    public String login() {

        logger.info("/login");
        return "login";

    }

}