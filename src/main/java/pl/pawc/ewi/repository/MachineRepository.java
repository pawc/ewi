package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.Machine;

import java.util.List;

@Repository
public interface MachineRepository extends CrudRepository<Machine, String> {

    List<Machine> findAll();

    List<Machine> findByIsActiveTrue();

}