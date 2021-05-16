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

    @Query(value = "select " +
            "ifnull(s.wartosc, 0) - ifnull(sum(round(z.wartosc * n.wartosc, 1)), 0)  " +
            "+ ifnull(sum(z.zatankowano), 0) - ifnull(sum(z.ogrzewanie), 0)  as suma " +
            "from maszyna m " +
            "join norma n on m.id = n.maszyna_id " +
            "left join stan s on n.id = s.norma_id and n.id = ?1 and s.rok = ?2 and s.miesiac = ?3 " +
            " left join dokument d on m.id = d.maszyna_id and year(d.data) = ?2 and month(d.data) = ?3 " +
            " left join zuzycie z on d.numer = z.dokument_numer and n.id = z.norma_id " +
            "    where n.id = ?1 ;",
        nativeQuery = true
    )
    Double getSuma(long normaId, int year, int month);


    @Query(value = "select ifnull(k.wartosc, 0)  + sum(ifnull(d.kilometry, 0)) as sumaKilometry " +
            "from kilometry k " +
            "left join dokument d on d.maszyna_id = k.maszyna_id " +
            "and year(d.data) = k.rok and month(d.data) = k.miesiac " +
            "where k.maszyna_id = ?1 and k.rok = ?2 and k.miesiac = ?3 ;",
          nativeQuery = true)
    Double getSumaKilometry(String maszynaId, int year, int month);

}