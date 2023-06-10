package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.model.RaportRoczny;

@Repository
public interface RaportRocznyRepository extends CrudRepository<RaportRoczny, String> {
}