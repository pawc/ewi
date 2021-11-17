package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Maszyna;

import java.util.List;

@Repository
public interface MaszynaRepository extends CrudRepository<Maszyna, String> {

    @Query("SELECT m FROM Maszyna m WHERE m.aktywna = true")
    List<Maszyna> findAllActive();

}