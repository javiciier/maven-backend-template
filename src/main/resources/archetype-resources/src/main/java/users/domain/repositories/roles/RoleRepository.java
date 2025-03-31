package ${package}.users.domain.repositories.roles;

import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Lazy
public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByName(RoleNames name);
}