package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Zuzycie;

import java.util.List;

@Repository
public interface ZuzycieRepository extends CrudRepository<Zuzycie, Long> {

    List<Zuzycie> findByDokument(Dokument dokument);
    List<Zuzycie> findByNorma(Norma norma);

}