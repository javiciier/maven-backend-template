package ${package}.auth.application.usecases.register;

import ${package}.auth.application.usecases.AuthBaseTest;
import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.common.security.PasswordEncoderConfiguration;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ${package}.users.domain.repositories.UserRepository;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(PasswordEncoderConfiguration.class)
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

  private User registeredUser;

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
    String nickname = faker.name().firstName();
    User user = generateUser(nickname);
    generateCredentialForUser(user, nickname);

    // Then
    RegisterUserParamsDTO dto = toRegisterUserParamsDTO(user);
    registeredUser = useCase.register(dto);
    // Check
    UUID registeredUserID = registeredUser.getUserID();
    Optional<User> optionalActualUser = userRepository.findUserWithCredentialsAndContactInfoAndRoleAssignmentsAndRole(registeredUserID);

    assert (optionalActualUser.isPresent());
    final User actualUser = optionalActualUser.get();
    assertAll(

            // Contains same data
            () -> assertEquals(registeredUser, actualUser),
            // User has credentials to access the system
            () -> assertNotNull(actualUser.getCredential()),
            // Password is hashed
            () -> assertNotEquals(dto.plainPassword(), actualUser.getCredential().getPasswordHash()),
            // User has default role assigned
            () -> assertEquals(1, registeredUser.getRoles().size()),
            () -> assertEquals(RoleNames.BASIC, registeredUser.getRoles().get(0).getName())
    );
  }

  @Test
  void registerDuplicatedUser() throws UserAlreadyExistsException {
    // Given
    String nickname = faker.name().firstName();
    User user = generateUser(nickname);
    generateCredentialForUser(user, nickname);
    final String expectedMessage = "A user with nickname '%s' already exists".formatted(nickname);

    // Then
    RegisterUserParamsDTO dto = toRegisterUserParamsDTO(user);
    registeredUser = useCase.register(dto);
    UUID registeredUserID = registeredUser.getUserID();
    final Exception thrownException = assertThrows(
            UserAlreadyExistsException.class,
            () -> useCase.register(dto)
    );
    String actualMessage = thrownException.getMessage();

    // Check
    assertAll(
            // Throws expected exception
            () -> assertTrue(thrownException instanceof UserAlreadyExistsException),
            // Contains expected message
            () -> assertEquals(expectedMessage, actualMessage)
    );
  }

  // endregion TEST CASES

}