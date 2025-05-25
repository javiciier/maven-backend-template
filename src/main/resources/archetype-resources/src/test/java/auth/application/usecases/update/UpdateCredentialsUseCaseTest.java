package ${package}.auth.application.usecases.register;

import ${package}.auth.application.usecases.AuthBaseTest;
import ${package}.auth.application.usecases.login.LoginUserUseCase;
import ${package}.auth.application.usecases.update.UpdateCredentialsUseCase;
import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.common.security.PasswordEncoderConfiguration;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
@Import(PasswordEncoderConfiguration.class)
@ActiveProfiles("test")
@DisplayName("auth.update.UpdateCredentialsUseCase")
class UpdateCredentialsUseCaseTest extends AuthBaseTest {

  // region DEPENDENCIES
  private final UpdateCredentialsUseCase updateCredentialsUseCase;
  private final LoginUserUseCase loginUseCase;
  // endregion DEPENDENCIES

  @Autowired
  public UpdateCredentialsUseCaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UpdateCredentialsUseCase updateCredentialsUseCase, LoginUserUseCase loginUseCase) {
    super(passwordEncoder, userRepository);
    this.updateCredentialsUseCase = updateCredentialsUseCase;
    this.loginUseCase = loginUseCase;
  }

  private User registeredUser;

  // region TEST LIFECYCLE
  @BeforeEach
  void registerUser()  {
    String nickname = faker.name().firstName();
    log.debug("Registering user with nickname '{}'", nickname);
    this.registeredUser = createAndRegisterUser(nickname);
  }

  @AfterEach
  void cleanUp() {
    userRepository.delete(registeredUser);
  }

  // endregion TEST LIFECYCLE

  // region TEST CASES

  @Test
  void updatePassword() throws UserNotFoundException, PasswordsMismatchException, IncorrectLoginException {
    // Given
    UUID registeredUserID = registeredUser.getUserID();
    String userNickname = registeredUser.getNickname();
    String oldPassword = getPlainPasswordFromUser(registeredUser);
    String newPassword = "new" + oldPassword;

    // Then
    updateCredentialsUseCase.updatePassword(registeredUserID, oldPassword, newPassword);
    final User loggedUser = loginUseCase.loginWithNicknameAndPassword(userNickname, newPassword);

    // Check
    assertAll(
            // User logged in
            () -> assertNotNull(loggedUser)
    );
  }

  @Test
  void updateInvalidPassword() throws UserNotFoundException, PasswordsMismatchException, IncorrectLoginException {
    // Given
    UUID registeredUserID = registeredUser.getUserID();
    String userNickname = registeredUser.getNickname();
    String oldPasswordInvalid = "invalid" + getPlainPasswordFromUser(registeredUser);
    String newPassword = "new" + oldPasswordInvalid;
    String expectedMessage = null;

    // Then
    final Exception thrownException = assertThrows(
            PasswordsMismatchException.class,
            () -> updateCredentialsUseCase.updatePassword(registeredUserID, oldPasswordInvalid, newPassword)
    );
    String actualMessage = thrownException.getMessage();

    // Check
    assertAll(
            // Throws expected exception
            () -> assertTrue(thrownException instanceof PasswordsMismatchException),
            // Contains expected message
            () -> assertEquals(expectedMessage, actualMessage)
    );
  }

  @Test
  void updatePasswordNonExistentUser() throws UserNotFoundException, PasswordsMismatchException, IncorrectLoginException {
    // Given
    UUID registeredUserID = registeredUser.getUserID();
    String userNickname = registeredUser.getNickname();
    String oldPassword = getPlainPasswordFromUser(registeredUser);
    String newPassword = "new" + oldPassword;
    userRepository.delete(registeredUser);
    final String expectedMessage = "User with ID '%s' not found".formatted(registeredUserID);

    // Then
    final Exception thrownException = assertThrows(
            UserNotFoundException.class,
            () -> updateCredentialsUseCase.updatePassword(registeredUserID, oldPassword, newPassword)
    );
    String actualMessage = thrownException.getMessage();

    // Check
    assertAll(
            // Throws expected exception
            () -> assertTrue(thrownException instanceof UserNotFoundException),
            // Contains expected message
            () -> assertEquals(expectedMessage, actualMessage)
    );
  }

  // endregion TEST CASES

}