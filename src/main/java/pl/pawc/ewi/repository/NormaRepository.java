package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Norma;

import java.util.List;

@Repository
public interface NormaRepository extends CrudRepository<Norma, Long> {

    @Query("SELECT n FROM Norma n WHERE n.maszyna.id = ?1")
    List<Norma> findByMaszynaId(String maszynaId);

}