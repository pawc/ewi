package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Unit;

import java.util.List;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    List<Unit> findAll();

}