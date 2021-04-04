package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Maszyna;

@Repository
public interface MaszynaRepository extends CrudRepository<Maszyna, String> {

}