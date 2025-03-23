package $

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;{package}.users.domain.entities.User;

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

  @Column(name = "assignedat", nullable = false)
  private LocalDateTime assignedAt;

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

    // endregion Relationships

  @PrePersist
  protected void onCreate() {
    this.assignedAt = LocalDateTime.now();
  }

    // region Domain-Model methods
    // endregion Domain-Model methods
}
