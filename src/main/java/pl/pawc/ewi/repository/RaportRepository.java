package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.Raport;

import java.util.List;

@Repository
public interface RaportRepository extends CrudRepository<Raport, String> {

    @Query(value = "select concat(m.nazwa, ' (', m.id, ')') as maszyna, n.jednostka, "+
        "round(sum(z.wartosc*n.wartosc), 2) as suma, concat(m.nazwa, n.jednostka) as id "+
        "from Maszyna m " +
        "join Dokument d on m.id = d.maszyna_id " +
        "join Zuzycie z on d.numer = z.dokument_numer " +
        "join Norma n on z.norma_id = n.id " +
        "where year(d.data) = ?1 and month(d.data) = ?2 " +
        "group by m.nazwa, n.jednostka",
        nativeQuery = true
    )
    List<Raport> getRaport(int year, int month);

}