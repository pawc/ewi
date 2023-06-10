package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;

@Repository
public interface KilometryRepository extends CrudRepository<Kilometry, String> {

   Kilometry findOneByMaszynaAndRokAndMiesiac(Maszyna maszyna, int rok, int miesiac);

}