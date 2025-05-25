package ${package}.auth.application.usecases.login;

import ${package}.auth.application.usecases.AuthBaseTest;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.common.security.PasswordEncoderConfiguration;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
@Import(PasswordEncoderConfiguration.class)
@ActiveProfiles("test")
@DisplayName("auth.login.LoginUserUseCase")
class LoginUserUseCaseTest extends AuthBaseTest {

  // region DEPENDENCIES
  private final LoginUserUseCase useCase;
  // endregion DEPENDENCIES

  @Autowired
  public LoginUserUseCaseTest(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, LoginUserUseCase useCase) {
    super(passwordEncoder, userRepository);
    this.useCase = useCase;
  }

  private User registeredUser;
  private String nickname;

  // region TEST LIFECYCLE
  @BeforeEach
  void registerUser()  {
    this.nickname = faker.name().firstName();
    log.debug("Registering user with nickname '{}'", this.nickname);
    this.registeredUser = createAndRegisterUser(this.nickname);
  }

  @AfterEach
  void cleanUp() {
    userRepository.delete(registeredUser);
  }

  // endregion TEST LIFECYCLE

  // region TEST CASES
  // region loginWithNicknameAndPassword USE CASE
  @Test
  void loginWithNicknameAndPassword() throws IncorrectLoginException {
    // Given
    String plainPassword = getPlainPasswordFromUser(registeredUser);

    // Then
    final User loggedUser = useCase.loginWithNicknameAndPassword(nickname, plainPassword);

    // Check
    assertAll(
            // A user was found
            () -> assertNotNull(loggedUser),
            // Is same user
            () -> assertEquals(this.registeredUser, loggedUser)
    );
  }

  @Test
  void loginWithNicknameAndPasswordNonExistentUser() throws UserAlreadyExistsException {
    // Given
    String nonExistentNickname = faker.name().firstName();
    final String plainPassword = getPlainPasswordFromUser(registeredUser);
    String expectedMessage = null;

    // Then
    final Exception thrownException = assertThrows(
            IncorrectLoginException.class,
            () -> useCase.loginWithNicknameAndPassword(nonExistentNickname, plainPassword)
    );
    String actualMessage = thrownException.getMessage();

    // Check
    assertAll(
            // Throws expected exception
            () -> assertTrue(thrownException instanceof IncorrectLoginException),
            // Contains expected message
            () -> assertEquals(expectedMessage, actualMessage)
    );
  }

  @Test
  void loginWithInvalidPassword() throws UserAlreadyExistsException {
    // Given
    final String plainPassword = "invalid" + getPlainPasswordFromUser(registeredUser);
    String expectedMessage = null;

    // Then
    final Exception thrownException = assertThrows(
            IncorrectLoginException.class,
            () -> useCase.loginWithNicknameAndPassword(this.nickname, plainPassword)
    );
    String actualMessage = thrownException.getMessage();

    // Check
    assertAll(
            // Throws expected exception
            () -> assertTrue(thrownException instanceof IncorrectLoginException),
            // Contains expected message
            () -> assertEquals(expectedMessage, actualMessage)
    );
  }

  // endregion loginWithNicknameAndPassword USE CASE

  // region loginWithJsonWebToken USE CASE
  @Test
  void loginWithJsonWebToken() throws UserNotFoundException {
    // Given
    UUID registeredUserID = this.registeredUser.getUserID();

    // Then
    final User loggedUser = useCase.loginWithJsonWebToken(registeredUserID);

    // Check
    assertAll(
            // A user was found
            () -> assertNotNull(loggedUser),
            // Is same user
            () -> assertEquals(this.registeredUser, loggedUser)
    );
  }

  @Test
  void loginWithJsonWebTokenNonExistentUser() throws UserAlreadyExistsException {
    // Given
    UUID nonExistentUserID = UUID.randomUUID();
    final String expectedMessage = "User with ID '%s' not found".formatted(nonExistentUserID);

    // Then
    final Exception thrownException = assertThrows(
            UserNotFoundException.class,
            () -> useCase.loginWithJsonWebToken(nonExistentUserID)
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

  // region loginWithJsonWebToken USE CASE

  // endregion TEST CASES

}