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
    public static final String MACHINES = "machines";
    public static final String CATEGORIES = "categories";
    public static final String UNITS = "units";
    private final MachineService machineService;
    private final CategoryService categoryService;
    private final UnitService unitService;

    @RequestMapping("/")
    public String index(
            Model model){

        logger.info("/");
        return "index";

    }

    @RequestMapping("/quarterlyReport")
    public String quarterlyReport(
            Model model){

        logger.info("/quarterlyReport");
        return "quarterlyReport";

    }

    @RequestMapping("/annualReport")
    public String annualReport(
            Model model){

        logger.info("/annualReport");
        return "annualReport";

    }

    @RequestMapping("/machineKilometersReport")
    public String machineKilometersReport(
            Model model){

        model.addAttribute(MACHINES, machineService.findAllActive());
        logger.info("/machineKilometersReport");
        return "machineKilometersReport";

    }

    @RequestMapping("/documentsView")
    public String documentsView(
            Model model) {

        logger.info("/documentsView");
        model.addAttribute(MACHINES, machineService.findAllActive());

        return "documents";

    }

    @RequestMapping("/machines")
    public String machines(
            Model model) {

        logger.info("/machines");
        model.addAttribute(MACHINES, machineService.findAll());
        model.addAttribute(CATEGORIES, categoryService.findAll());
        model.addAttribute(UNITS, unitService.findAll());

        return MACHINES;

    }

    @RequestMapping("/unitsView")
    public String unitsView(
            Model model) {

        logger.info("/unitsView");
        model.addAttribute(UNITS, unitService.findAll());

        return UNITS;

    }

    @RequestMapping("/initialStatesView")
    public String initialStatesView(Model model) {

        logger.info("/initialStates");
        return "initialStates";

    }

    @RequestMapping("/kilometersView")
    public String kilometersView(
            Model model) {

        logger.info("/kilometers");
        return "kilometers";

    }

    @RequestMapping("/categories")
    public String categories(
            Model model) {

        logger.info("/categories");

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

        model.addAttribute(CATEGORIES, kategorie);

        return CATEGORIES;

    }

    @RequestMapping("/login")
    public String login() {

        logger.info("/login");
        return "login";

    }

}