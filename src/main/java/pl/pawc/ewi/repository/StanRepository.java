package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.StanRaport;

import java.util.List;

@Repository
public interface StanRepository extends CrudRepository<Stan, String> {

    @Query("FROM Stan s WHERE s.norma = ?1 and s.rok = ?2 and s.miesiac = ?3")
    List<Stan> findBy(Norma norma, int rok, int miesiac);

    @Query(value = "SELECT " +
        "m.nazwa as maszynanazwa," +
        "m.id as maszynaid, " +
        "n.jednostka, " +
        "n.id as normaid, " +
        "if(s.id is null, -1, s.id) as stanid, " +
        "IF(s.wartosc IS NULL, 0, s.wartosc) AS stanpoczatkowy " +
        "FROM norma n " +
        "JOIN maszyna m ON n.maszyna_id = m.id " +
        "left join stan s ON n.id = s.norma_id " +
        "AND s.rok = ?1 AND s.miesiac = ?2", nativeQuery = true)
    List<StanRaport> findBy(int rok, int miesiac);

}