package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Kategoria;

@Repository
public interface KategoriaRepository extends CrudRepository<Kategoria, String> {
}