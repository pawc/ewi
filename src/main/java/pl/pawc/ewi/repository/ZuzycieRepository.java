package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Zuzycie;

@Repository
public interface ZuzycieRepository extends CrudRepository<Zuzycie, Long> {

}