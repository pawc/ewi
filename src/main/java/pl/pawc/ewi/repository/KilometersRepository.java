package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;

import java.util.Optional;

@Repository
public interface KilometersRepository extends CrudRepository<Kilometers, String> {

   Optional<Kilometers> findOneByMachineAndYearAndMonth(Machine machine, int year, int month);

}