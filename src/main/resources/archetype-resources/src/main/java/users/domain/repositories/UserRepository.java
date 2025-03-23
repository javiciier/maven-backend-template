package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

@Lazy
public interface UserRepository extends ListCrudRepository<User, UUID> {

}