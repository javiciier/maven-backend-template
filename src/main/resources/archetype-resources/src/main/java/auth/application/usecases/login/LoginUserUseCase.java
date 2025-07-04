package ${package}.auth.application.usecases.login;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.users.domain.entities.Credential;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.CredentialRepository;
import ${package}.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Lazy
public class LoginUserUseCase {

  // region DEPENDENCIES
  private final AuthUtils authUtils;
  private final UserRepository userRepository;
  private final CredentialRepository credentialRepository;
  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Login with the given {@code nickname} and {@code password}
   *
   * @param nickname      User nickname
   * @param plainPassword User plain password
   * @return User succesfuly logged in
   * @throws IncorrectLoginException Incorrect login parameters
   */
  public User loginWithNicknameAndPassword(String nickname, String plainPassword)
      throws IncorrectLoginException {
    log.debug("Trying to do nickname/password login for user with nickname '{}'", nickname);

    // Retrieve user
    User user;
    try {
      user = credentialRepository
          .findByNicknameIgnoreCase(nickname)
          .orElseThrow(() -> new UserNotFoundException(nickname))
          .getUser();
    } catch (UserNotFoundException ex) {
      throw new IncorrectLoginException();
    }

    // Check if credentials match
    Credential userCredential = user.getCredential();
    if (!authUtils.doPasswordsMatch(plainPassword, userCredential.getPasswordHash())) {
      throw new IncorrectLoginException();
    }

    log.debug("User with nickname '{}' logged in succesfuly using nickname/password", nickname);
    return user;
  }

  /**
   * Login the user with the given {@code userID}
   *
   * @param userID User ID
   * @return User succesfuly logged in
   * @throws UserNotFoundException User with the given ID does not exist
   */
  public User loginWithJsonWebToken(UUID userID) throws UserNotFoundException {
    log.debug("Trying to do JWT login for user with ID '{}'", userID);

    User user = userRepository.findUserWithCredentialsAndContactInfoAndRoleAssignmentsAndRole(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));

    log.debug("User with nickname '{}' logged in succesfuly using JWT", user.getNickname());
    return user;
  }

  // endregion USE CASES


}
