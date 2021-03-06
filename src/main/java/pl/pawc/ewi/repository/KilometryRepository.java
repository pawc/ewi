package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.model.KilometryRaport;

import java.util.List;

@Repository
public interface KilometryRepository extends CrudRepository<Kilometry, String> {

    @Query("SELECT k FROM Kilometry k WHERE k.maszyna.id = ?1 and k.rok = ?2 and k.miesiac = ?3")
    List<Kilometry> findBy(String maszynaId, int rok, int miesiac);

    @Query(value = "select m.id as maszynaid, m.nazwa as maszynanazwa, IF(k.wartosc IS NULL, 0, k.wartosc) as stanpoczatkowy " +
            "from maszyna m " +
            "left join kilometry k on m.id = k.maszyna_id " +
            "and k.rok = ?1 and k.miesiac = ?2 ;", nativeQuery = true)
    List<KilometryRaport> findBy(int rok, int miesiac);

}