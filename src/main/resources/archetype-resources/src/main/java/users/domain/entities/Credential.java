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
@Table(name = "Credential", schema = "users")
public class Credential {
    // region Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id", nullable = false)
    private Long credentialID;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "passwordEncrypted", nullable = false)
    private String passwordEncrypted;

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
