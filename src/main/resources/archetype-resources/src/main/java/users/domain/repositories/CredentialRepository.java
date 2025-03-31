package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.Credential;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.query.Param;

@Lazy
public interface CredentialRepository extends CrudRepository<Credential, Long> {

  /**
   * Comprueba si existe un usuario por su nombre de usuario, ignorando maýusculas o minúsculas.
   *
   * @param nickname Nombre del usuario
   * @return True si existe un usuario con el nombre recibido.
   */
  boolean existsByNicknameIgnoreCase(String nickname);

  Optional<Credential> findByNicknameIgnoreCase(String nickname);

  @Query("SELECT c FROM Credential c WHERE c.user.userID = :userID")
  Optional<Credential> findCredentialByUserID(@Param("userID") UUID userID);

}