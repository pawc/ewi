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

    @RequestMapping("/home")
    public String home(Model model){
        logger.debug("/home");
        return "home";
    }

    @RequestMapping("/quarterlyReport")
    public String quarterlyReport(
            Model model){

        logger.debug("/quarterlyReport");
        return "quarterlyReport";

    }

    @RequestMapping("/annualReport")
    public String annualReport(
            Model model){

        logger.debug("/annualReport");
        return "annualReport";

    }

    @RequestMapping("/machineKilometersReport")
    public String machineKilometersReport(
            Model model){

        model.addAttribute(MACHINES, machineService.findAllActive());
        logger.debug("/machineKilometersReport");
        return "machineKilometersReport";

    }

    @RequestMapping("/documentsView")
    public String documentsView(
            Model model) {

        logger.debug("/documentsView");
        model.addAttribute(MACHINES, machineService.findAllActive());

        return "documents";

    }

    @RequestMapping("/machines")
    public String getMachines(
            Model model) {

        logger.debug("/machines");
        model.addAttribute(MACHINES, machineService.findAll());
        model.addAttribute(CATEGORIES, categoryService.findAll());
        model.addAttribute(UNITS, unitService.findAll());

        return MACHINES;

    }

    @RequestMapping("/unitsView")
    public String unitsView(
            Model model) {

        logger.debug("/unitsView");
        model.addAttribute(UNITS, unitService.findAll());

        return UNITS;

    }

    @RequestMapping("/initialStatesView")
    public String initialStatesView(Model model) {

        logger.debug("/initialStates");
        return "initialStates";

    }

    @RequestMapping("/kilometersView")
    public String kilometersView(
            Model model) {

        logger.debug("/kilometers");
        return "kilometers";

    }

    @RequestMapping("/categories")
    public String getCategories(
            Model model) {

        logger.debug("/categories");

        List<Category> categories = Lists.newArrayList(categoryService.findAll());

        Iterable<Machine> allUncategorized = machineService.findAllUncategorized();
        Set<Machine> machines = Sets.newHashSet(allUncategorized);

        if(!machines.isEmpty()){
            Category category = new Category();

            category.setName("Nieprzydzielone");
            category.setMachines(machines);
            category.setCarriedOver(false);

            categories.add(category);
        }

        model.addAttribute(CATEGORIES, categories);

        return CATEGORIES;

    }

}