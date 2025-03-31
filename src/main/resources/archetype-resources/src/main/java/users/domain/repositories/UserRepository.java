package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Lazy
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
  @EntityGraph(
      attributePaths = {"credential", "contactInfo", "roleAssignments", "roleAssignments.role"}
  )
  @Query("SELECT u FROM User u WHERE u.userID = :userID")
  Optional<User> findCompleteUserByUserID(@Param("userID") UUID userID);
}