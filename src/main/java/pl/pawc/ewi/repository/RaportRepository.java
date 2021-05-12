package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.Raport;

import java.util.List;

@Repository
public interface RaportRepository extends CrudRepository<Raport, String> {

    @Query(value = "select concat(m.nazwa, ' (', m.id, ')') as maszyna, " +
            "m.id as maszynaid, " +
            "IF(k.wartosc IS NULL, 0, k.wartosc) as stankilometry, " +
            "sum(round(d.kilometry, 1)) as kilometry, "+
            "n.jednostka, " +
            "sum(round(z.wartosc*n.wartosc, 1)) as suma, " +
            "sum(round(z.zatankowano, 1)) as zatankowano, " +
            "sum(round(z.ogrzewanie, 1)) as ogrzewanie, " +
            "concat(m.nazwa, n.jednostka) as id, " +
            "z.norma_id , " +
            "IF(s.wartosc IS NULL, 0, s.wartosc) AS 'stan_poprz' " +
            "from maszyna m   " +
            "join dokument d on year(d.data) = ?1 and month(d.data) = ?2 " +
            "and m.id = d.maszyna_id   " +
            "join zuzycie z on d.numer = z.dokument_numer   " +
            "join norma n on z.norma_id = n.id " +
            "LEFT JOIN stan s ON n.id = s.norma_id AND s.rok = ?1 AND s.miesiac = ?2 " +
            "LEFT JOIN kilometry k ON m.id = k.maszyna_id and k.rok = ?1 and k.miesiac = ?2 " +
            "group by m.nazwa, n.jednostka",
        nativeQuery = true
    )
    List<Raport> getRaport(int year, int month);

}