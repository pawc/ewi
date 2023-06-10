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
    List<Dokument> findByDataBetween(Date start, Date end);
    List<Dokument> findByMaszynaAndDataBetween(Maszyna maszyna, Date start, Date end);

}