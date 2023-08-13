package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public boolean post(Category category){
        boolean exists = categoryRepository.findById(category.getName()).isPresent();
        if (!exists) categoryRepository.save(category);
        return !exists;
    }

    public void delete(Category category){
        categoryRepository
                .findById(category.getName())
                .ifPresent(categoryRepository::delete);
    }

    public boolean toggleCarriedOver(Category category){
        Optional<Category> byId = categoryRepository.findById(category.getName());
        boolean exists = byId.isPresent();
        if(exists){
            Category kat = byId.get();
            kat.setCarriedOver(!kat.isCarriedOver());
            categoryRepository.save(kat);
        }
        return exists;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findOneByName(String name){
        return categoryRepository.findOneByName(name);
    }

}