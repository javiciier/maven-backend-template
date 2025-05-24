package ${package}.users.domain.entities;

import ${package}.users.domain.entities.roles.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "user_id", nullable = false, updatable = false)
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
  public void assignCredential(Credential other) {
    other.setUser(this);
    this.credential = other;
  }

  @Transient
  public void assignContactInfo(ContactInfo other) {
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
    RoleAssignment roleAssignment = RoleAssignment.builder()
        .id(new RoleAssignmentID(this.userID, role.getRoleID()))
        .user(this)
        .role(role)
        .assignedAt(LocalDateTime.now())
        .build();
    this.roleAssignments.add(roleAssignment);
  }

  // endregion Domain-Model methods


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", userID=").append(userID);
    sb.append('}');

    return sb.toString();
  }
}
