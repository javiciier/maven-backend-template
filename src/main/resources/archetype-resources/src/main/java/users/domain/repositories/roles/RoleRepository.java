package ${package}.users.domain.repositories.roles;

import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleRepository extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(RoleNames name);
}