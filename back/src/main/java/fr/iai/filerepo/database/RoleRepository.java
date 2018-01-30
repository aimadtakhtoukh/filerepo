package fr.iai.filerepo.database;

import fr.iai.filerepo.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findOneByRoleName(String roleName);
}
