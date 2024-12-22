package $package.users.infrastructure.repositories;

import $package.users.domain.UserRole;
import $package.users.domain.UserRoleID;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleID> {
}
