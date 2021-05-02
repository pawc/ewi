package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Stan;

import java.util.List;

@Repository
public interface StanRepository extends CrudRepository<Stan, String> {

    @Query("SELECT s FROM Stan s WHERE s.norma.id = ?1 and s.rok = ?2 and s.miesiac = ?3")
    List<Stan> findBy(long normaId, int rok, int miesiac);

}