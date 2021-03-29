package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Dokument;

@Repository
public interface DokumentRepository extends CrudRepository<Dokument, String> {

}