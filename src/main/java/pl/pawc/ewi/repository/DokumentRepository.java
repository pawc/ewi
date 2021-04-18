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

}