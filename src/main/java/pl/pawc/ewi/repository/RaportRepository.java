package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.Raport;

@Repository
public interface RaportRepository extends CrudRepository<Raport, String> {
}