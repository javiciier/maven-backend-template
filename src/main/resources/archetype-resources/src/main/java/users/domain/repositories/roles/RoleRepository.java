package $

import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.ListCrudRepository;

@Lazy
public interface RoleRepository extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(RoleNames name);
}