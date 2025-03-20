package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.Credential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends ListCrudRepository<Credential, Long> {
    /**
     * Comprueba si existe un usuario por su nombre de usuario, ignorando maýusculas o minúsculas.
     *
     * @param nickname Nombre del usuario
     * @return True si existe un usuario con el nombre recibido.
     */
    boolean existsByNicknameIgnoreCase(String nickname);

    Optional<Credential> findByNicknameIgnoreCase(String nickname);

    @Query("SELECT c FROM Credential c WHERE c.user.userID = ?1")
    Optional<Credential> findCredentialByUserID(UUID userID);

}