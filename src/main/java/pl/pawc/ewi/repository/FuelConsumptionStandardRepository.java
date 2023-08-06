package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.Machine;

import java.util.List;

@Repository
public interface FuelConsumptionStandardRepository extends CrudRepository<FuelConsumptionStandard, Long> {

    List<FuelConsumptionStandard> findByMachine(Machine machine);

}