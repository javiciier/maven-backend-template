package ${package}.users.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "userrole", schema = "users")
public class UserRole {
    // region Atributes
    @EmbeddedId
    private UserRoleID id;

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
