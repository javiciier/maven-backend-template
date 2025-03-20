package ${package}.users.domain.entities;

import jakarta.persistence.*;
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

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Builder.Default
    @Column(name = "isemailverified", nullable = false)
    private Boolean isEmailVerified = false;

    @Column(name = "phonenumber", nullable = false, length = 20)
    private String phoneNumber;

    @Builder.Default
    @Column(name = "isphonenumberverified", nullable = false)
    private Boolean isPhoneNumberVerified = false;

    // endregion Atributes


    // region Relationships
    @ToString.Exclude
    @OneToOne(optional = false, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // endregion Relationships


    // region Domain-Model methods
    // endregion Domain-Model methods
}
