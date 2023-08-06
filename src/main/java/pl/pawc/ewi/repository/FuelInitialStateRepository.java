package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.FuelConsumptionStandard;

@Repository
public interface FuelInitialStateRepository extends CrudRepository<FuelInitialState, String> {

    FuelInitialState findOneByFuelConsumptionStandardAndYearAndMonth(FuelConsumptionStandard fuelConsumptionStandard, int year, int month);

}