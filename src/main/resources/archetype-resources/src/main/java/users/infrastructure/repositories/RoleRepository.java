package $package.users.infrastructure.repositories;

import $package.users.domain.Role;
import $package.users.domain.UserRoles;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleRepository extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(UserRoles name);
}
