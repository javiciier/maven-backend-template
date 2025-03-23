package $

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
