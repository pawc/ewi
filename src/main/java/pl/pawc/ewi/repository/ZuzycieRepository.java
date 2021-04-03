package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Zuzycie;

import java.util.List;

@Repository
public interface ZuzycieRepository extends CrudRepository<Zuzycie, Long> {

    @Query("SELECT z FROM Zuzycie z WHERE z.dokument.numer = ?1")
    List<Zuzycie> findByDokumentId(String dokumentId);

}