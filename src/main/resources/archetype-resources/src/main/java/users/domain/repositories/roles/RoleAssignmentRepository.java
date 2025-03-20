package ${package}.users.domain.repositories.roles;

import ${package}.users.domain.entities.roles.RoleAssignment;
import ${package}.users.domain.entities.roles.RoleAssignmentID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleAssignmentRepository extends ListCrudRepository<RoleAssignment, RoleAssignmentID> {
}