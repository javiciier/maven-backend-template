package ${package}.users.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "contactinfo", schema = "users")
public class ContactInfo {
    // region Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactinfo_id", nullable = false)
    private Long contactInfoID;

  @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "isemailverified", nullable = false)
    private Boolean isEmailVerified;

    @Column(name = "phonenumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "isphonenumberverified", nullable = false)
    private Boolean isPhoneNumberVerified;

    // endregion Atributes


    // region Relationships
    @ToString.Exclude
    @OneToOne(optional = false, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // endregion Relationships

  @PrePersist
  protected void onCreate() {
    if (isEmailVerified == null) {
      isEmailVerified = false;
    }
    if (isPhoneNumberVerified == null) {
      isPhoneNumberVerified = false;
    }
  }

    // region Domain-Model methods
    // endregion Domain-Model methods
}
