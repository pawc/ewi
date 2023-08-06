package pl.pawc.ewi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.Machine;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository<Document, String> {

    @Query(value = "from Document d where year(d.date) = ?1 and month(d.date) = ?2")
    List<Document> getDocuments(int year, int month);

    Optional<Document> findById(String numer);
    List<Document> findByMachine(Machine machine);
    List<Document> findByDateBetween(Date start, Date end);
    List<Document> findByMachineAndDateBetween(Machine machine, Date start, Date end);

}