package $

import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.ListCrudRepository;

@Lazy
public interface RoleAssignmentRepository extends ListCrudRepository<RoleAssignment, RoleAssignmentID> {
}