package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface UserRepository extends ListCrudRepository<User, UUID> {
}