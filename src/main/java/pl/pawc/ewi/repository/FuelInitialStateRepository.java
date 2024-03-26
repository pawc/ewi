package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.FuelInitialState;

@Repository
public interface FuelInitialStateRepository extends CrudRepository<FuelInitialState, String> {

    FuelInitialState findOneByFuelConsumptionStandardAndYearAndMonth(FuelConsumptionStandard fuelConsumptionStandard, int year, int month);

}