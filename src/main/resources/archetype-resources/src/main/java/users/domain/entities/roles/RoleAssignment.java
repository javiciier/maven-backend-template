package ${package}.users.domain.entities.roles;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "roleasiggnment", schema = "users")
public class RoleAssignment {
    // region Atributes
    @EmbeddedId
    private RoleAssignmentID id;

    // endregion Atributes

    // region Relationships
    @ToString.Exclude
    @MapsId("userID")
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @MapsId("roleID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder.Default
    @Column(name = "assignedat", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();


    // endregion Relationships


    // region Domain-Model methods
    // endregion Domain-Model methods
}
