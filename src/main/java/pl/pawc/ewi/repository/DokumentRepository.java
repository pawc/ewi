package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;

import java.util.Date;
import java.util.List;

@Repository
public interface DokumentRepository extends CrudRepository<Dokument, String> {

    @Query(value = "from Dokument d where year(d.data) = ?1 and month(d.data) = ?2")
    List<Dokument> getDokumenty(int year, int month);

    List<Dokument> findByMaszyna(Maszyna maszyna);

    @Query(value = "select " +
            "ifnull(s.wartosc, 0) - ifnull(sum(round(CAST((z.wartosc * n.wartosc) AS DECIMAL(14, 4)), 1)), 0)  " +
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

    @Query(value = "select round(" +
            "ifnull(s.wartosc, 0) - ifnull(sum(round(CAST((z.wartosc * n.wartosc) AS DECIMAL(14, 4)), 1)), 0)  " +
            "+ ifnull(sum(z.zatankowano), 0) - ifnull(sum(z.ogrzewanie), 0), 1)  as suma " +
            "from maszyna m " +
            "join norma n on m.id = n.maszyna_id " +
            "left join stan s on n.id = s.norma_id and n.id = ?1 and s.rok = ?2 and s.miesiac = ?3 " +
            " left join dokument d on m.id = d.maszyna_id and year(d.data) = ?2 and month(d.data) = ?3 " +
            " left join zuzycie z on d.numer = z.dokument_numer and n.id = z.norma_id " +
            "    where n.id = ?1 and d.data <= ?4 and d.numer <> ?5 ;",
            nativeQuery = true
    )
    Double getSumBeforeDate(long normaId, int year, int month, Date date, String docExcluded);

    @Query(value = "select round(ifnull(k.wartosc, 0)  + sum(ifnull(d.kilometry, 0)), 2) as sumaKilometry " +
            "from maszyna m " +
            "left join kilometry k on m.id = k.maszyna_id " +
            "left join dokument d on m.id = d.maszyna_id and year(d.data) = k.rok and month(d.data) = k.miesiac " +
            "where m.id = ?1 and k.rok = ?2 and k.miesiac = ?3 ;",
          nativeQuery = true)
    Double getSumaKilometry(String maszynaId, int year, int month);

    @Query(value = "select round(ifnull(k.wartosc, 0)  + sum(ifnull(d.kilometry, 0)), 2) as sumaKilometry " +
            "from dokument d " +
            "left join kilometry k on d.maszyna_id = k.maszyna_id " +
            "and year(d.data) = k.rok and month(d.data) = k.miesiac " +
            "where d.maszyna_id = ?1 and year(d.data) = ?2 and month(d.data) = ?3 " +
            "and d.data <= ?4 and d.numer <> ?5 ;",
            nativeQuery = true)
    Double getSumaKilometryBeforeDate(String maszynaId, int year, int month, Date date, String docExcluded);

}