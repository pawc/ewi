package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Maszyna;

import java.util.List;

@Repository
public interface MaszynaRepository extends CrudRepository<Maszyna, String> {

    @Query("FROM Maszyna m WHERE m.aktywna = true")
    @Deprecated()
    List<Maszyna> findAllActive();

    @Query(value = "SELECT m.* FROM maszyna m LEFT JOIN maszyna_kategorie mk ON m.id = mk.maszyna_id WHERE mk.maszyna_id IS NULL",
    nativeQuery = true)
    @Deprecated
    Iterable<Maszyna> findAllUncategorized();

}