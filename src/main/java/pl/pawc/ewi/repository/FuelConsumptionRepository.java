package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.FuelConsumption;
import pl.pawc.ewi.entity.FuelConsumptionStandard;

import java.util.List;

@Repository
public interface FuelConsumptionRepository extends CrudRepository<FuelConsumption, Long> {

    List<FuelConsumption> findByDocument(Document document);
    List<FuelConsumption> findByFuelConsumptionStandard(FuelConsumptionStandard fuelConsumptionStandard);

}