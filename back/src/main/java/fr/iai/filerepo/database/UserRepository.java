package fr.iai.filerepo.database;

import fr.iai.filerepo.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    List<User> findAll();

}
