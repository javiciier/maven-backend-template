package $

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
    private Set<RoleAssignment> assignations = new HashSet<>();

    // endregion Relationships


    // region Domain-Model methods

    // endregion Domain-Model methods
}
