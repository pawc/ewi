package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.KilometryRaport;

import java.util.List;

@Repository
public interface KilometryRepository extends CrudRepository<Kilometry, String> {

    @Query("FROM Kilometry k WHERE k.maszyna = ?1 and k.rok = ?2 and k.miesiac = ?3")
    @Deprecated
    List<Kilometry> findBy(Maszyna maszyna, int rok, int miesiac);

    Kilometry findOneByMaszynaAndRokAndMiesiac(Maszyna maszyna, int rok, int miesiac);

    @Query(value = "select m.id as maszynaid, m.nazwa as maszynanazwa, IF(k.wartosc IS NULL, 0, k.wartosc) as stanpoczatkowy " +
            "from maszyna m " +
            "left join kilometry k on m.id = k.maszyna_id " +
            "and k.rok = ?1 and k.miesiac = ?2 ;", nativeQuery = true)
    @Deprecated
    List<KilometryRaport> findBy(int rok, int miesiac);

}