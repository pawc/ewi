package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.service.CategoryService;

@RequiredArgsConstructor
@RestController
public class CategoryRestController {

    private static final Logger logger = LogManager.getLogger(CategoryRestController.class);
    private final CategoryService categoryService;

    @PostMapping("/category")
    public void post(
            @RequestBody Category category){

        if(categoryService.post(category)) logger.info("/kategoria POST {}", category.getName());
        else {
            logger.warn("/category POST {} - category already exists", category.getName());
            throw new BadRequestException();
        }

    }

    @DeleteMapping("/category")
    public void delete(
            @RequestBody Category category){

        logger.info("/category DELETE {}", category.getName());
        categoryService.delete(category);

    }

    @PutMapping("/toggleCarriedOver")
    public void toggle(
            @RequestBody Category category) {

        logger.info("/toggleCarriedOver PUT {}", category.getName());
        if(!categoryService.toggleCarriedOver(category)) throw new BadRequestException();

    }

}