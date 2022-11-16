package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Jednostka;

import java.util.List;

@Repository
public interface JednostkaRepository extends CrudRepository<Jednostka, Long> {

    List<Jednostka> findAll();

}