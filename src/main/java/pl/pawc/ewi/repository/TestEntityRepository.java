package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.TestEntity;

@Repository
public interface TestEntityRepository  extends CrudRepository<TestEntity, Long> {

}