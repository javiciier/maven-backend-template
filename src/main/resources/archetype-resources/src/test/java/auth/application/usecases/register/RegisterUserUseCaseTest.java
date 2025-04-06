package ${package}.auth.application.usecases.register;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ${package}.auth.application.usecases.AuthBaseTest;
import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
import ${package}.users.domain.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("auth.login.RegisterUserUseCase")
class RegisterUserUseCaseTest extends AuthBaseTest {

  // region DEPENDENCIES
  private final RegisterUserUseCase useCase;
  // endregion DEPENDENCIES

  @Autowired
  public RegisterUserUseCaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RegisterUserUseCase useCase) {
    super(passwordEncoder, userRepository);
    this.useCase = useCase;
  }

  User registeredUser;

  // region TEST LIFECYCLE
  @AfterEach
  void cleanUp() {
    userRepository.delete(registeredUser);
  }

  // endregion TEST LIFECYCLE

  // region TEST CASES

  @Test
  void register() throws UserAlreadyExistsException {
    // Given
    String nickname = this.getClass().getSimpleName();
    User user = generateUser(nickname);
    generateCredentialForUser(user, nickname);

    // Then
    RegisterUserParamsDTO dto = toRegisterUserParamsDTO(user);
    registeredUser = useCase.register(dto);
    // Check
    UUID registeredUserID = registeredUser.getUserID();
    Optional<User> optionalActualUser = userRepository.findCompleteUserByUserID(registeredUserID);

    assert (optionalActualUser.isPresent());
    User actualUser = optionalActualUser.get();
    assertAll(
        // Contains same data
        () -> assertEquals(registeredUser, actualUser),
        // User has credentials to access the system
        () -> assertNotNull(actualUser.getCredential()),
        // Password is hashed
        () -> assertNotEquals(dto.getPlainPassword(), actualUser.getCredential().getPasswordHash())
    );
  }

  // endregion TEST CASES

}