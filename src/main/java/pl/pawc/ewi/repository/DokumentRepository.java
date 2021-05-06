package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Dokument;

import java.util.List;

@Repository
public interface DokumentRepository extends CrudRepository<Dokument, String> {

    @Query(value = "select * from dokument d where year(d.data) = ?1 and month(d.data) = ?2",
        nativeQuery = true
    )
    List<Dokument> getDokumenty(int year, int month);

    @Query(value = "select sum(round(z.wartosc * n.wartosc, 1)) as suma from norma n " +
        "join zuzycie z on n.id = z.norma_id " +
        "join dokument d on z.dokument_numer = d.numer " +
        "where n.id = ?1 and year(d.data) = ?2 and month(d.data) = ?3",
        nativeQuery = true
    )
    Double getSuma(long normaId, int year, int month);

}