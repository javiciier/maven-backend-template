package $

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "userTable", schema = "users")
public class User {
    // region Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID userID;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", length = 50)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;

    @Column(name = "registeredat", nullable = false)
    private LocalDateTime registeredAt;

    // endregion Atributes

    // region Relationships
    @ToString.Exclude
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Credential credential;

  @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ContactInfo contactInfo;

  @Builder.Default
    @ToString.Exclude
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private Set<RoleAssignment> roleAssignments = new HashSet<>();

    // endregion Relationships

  @PrePersist
  protected void onCreate() {
    this.registeredAt = LocalDateTime.now();
  }

    // region Getters
    public String getNickname() {
        return getCredential().getNickname();
    }

    // endregion Getters

    // region Domain-Model methods
    @Transient
    public void attachCredential(Credential other) {
        other.setUser(this);
        this.credential = other;
    }

    @Transient
    public void attachContactInfo(ContactInfo other) {
        other.setUser(this);
        this.contactInfo = other;
    }


    @Transient
    public List<Role> getRoles() {
        return roleAssignments.stream()
                .map(RoleAssignment::getRole)
                .toList();
    }

    @Transient
    public void assignRole(Role role) {
        RoleAssignmentID roleAssignmentID = new RoleAssignmentID(this.userID, role.getRoleID());
        RoleAssignment roleAssignment = RoleAssignment.builder()
                .id(roleAssignmentID)
                .user(this)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .build();
        this.roleAssignments.add(roleAssignment);
    }

    // endregion Domain-Model methods

}
