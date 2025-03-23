package ${package}.users.domain.repositories.roles;

import ${package}.users.domain.entities.roles.RoleAssignment;
import ${package}.users.domain.entities.roles.RoleAssignmentID;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.ListCrudRepository;

@Lazy
public interface RoleAssignmentRepository extends
    ListCrudRepository<RoleAssignment, RoleAssignmentID> {

}