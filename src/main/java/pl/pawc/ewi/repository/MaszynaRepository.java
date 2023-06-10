package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Maszyna;

import java.util.List;

@Repository
public interface MaszynaRepository extends CrudRepository<Maszyna, String> {

    List<Maszyna> findAll();

    List<Maszyna> findByAktywnaTrue();

}