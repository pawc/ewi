package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.Raport;

import java.util.List;

@Repository
public interface RaportRepository extends CrudRepository<Raport, String> {

    @Query(value = "select concat(m.nazwa, ' (', m.id, ')') as maszyna, " +
            "n.jednostka, " +
            "sum(round(z.wartosc*n.wartosc, 1)) as suma, " +
            "sum(round(z.zatankowano, 1)) as zatankowano, " +
            "concat(m.nazwa, n.jednostka) as id, " +
            "z.norma_id " +
            "from maszyna m  " +
            "join dokument d on year(d.data) = ?1 and month(d.data) = ?2  " +
            "and m.id = d.maszyna_id  " +
            "join zuzycie z on d.numer = z.dokument_numer  " +
            "join norma n on z.norma_id = n.id  " +
            "group by m.nazwa, n.jednostka;",
        nativeQuery = true
    )
    List<Raport> getRaport(int year, int month);

}