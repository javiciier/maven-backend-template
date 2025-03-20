package ${package}.users.domain.entities;

import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleAssignment;
import ${package}.users.domain.entities.roles.RoleAssignmentID;
import ${package}.users.domain.entities.roles.RoleNames;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder.Default
    @Column(name = "registeredat", nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    // endregion Atributes


    // region Relationships
    @ToString.Exclude
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Credential credential;

    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private ContactInfo contactInfo;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoleAssignment> roleAssignments = new ArrayList<>();

    // endregion Relationships

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
