package pl.pawc.ewi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class EwiController {

    @RequestMapping("/")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response){

        return "index";

    }

}