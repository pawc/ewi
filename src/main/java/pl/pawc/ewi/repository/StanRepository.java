package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;

@Repository
public interface StanRepository extends CrudRepository<Stan, String> {

    Stan findOneByNormaAndRokAndMiesiac(Norma norma, int year, int month);

}