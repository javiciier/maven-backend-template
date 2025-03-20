package ${package}.users.domain.entities.roles;

import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.entities.roles.RoleAssignment;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "role", schema = "users")
public class Role {
    // region Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long roleID;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleNames name;

    // endregion Atributes


    // region Relationships
    @ToString.Exclude
    @OneToMany(mappedBy = "role")
    private Set<RoleAssignment> assignations = new LinkedHashSet<>();

    // endregion Relationships


    // region Domain-Model methods

    // endregion Domain-Model methods
}
