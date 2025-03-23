package $

import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.ListCrudRepository;

@Lazy
public interface UserRepository extends ListCrudRepository<User, UUID> {
}