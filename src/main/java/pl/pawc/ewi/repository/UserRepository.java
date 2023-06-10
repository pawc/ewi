package pl.pawc.ewi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawc.ewi.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}