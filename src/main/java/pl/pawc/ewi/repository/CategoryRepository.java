package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Category;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {

    List<Category> findAll();
    Optional<Category> findOneByName(String name);

}