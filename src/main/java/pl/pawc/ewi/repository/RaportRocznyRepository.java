package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.RaportRoczny;

import java.util.List;

@Repository
public interface RaportRocznyRepository extends CrudRepository<RaportRoczny, String> {

    @Query(value = "select concat(k.nazwa, '-', n.jednostka) as 'kategoria_jednostka', " +
            "k.nazwa as 'kategoria', n.jednostka, sum(round(z.wartosc * n.wartosc, 2)) as 'suma' from kategoria k " +
            "join maszyna_kategorie mk on k.nazwa = mk.kategorie_nazwa " +
            "join maszyna m on mk.maszyna_id = m.id " +
            "join norma n on m.id = n.maszyna_id " +
            "join zuzycie z on n.id = z.norma_id " +
            "join dokument d on z.dokument_numer = d.numer " +
            "where year(d.data) = ?1 " +
            "group by k.nazwa + '-' + n.jednostka, k.nazwa, n.jednostka;",
        nativeQuery = true
    )
    List<RaportRoczny> getRaport(int year);

}