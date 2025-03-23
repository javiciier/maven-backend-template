package ${package}.auth.application.utils;

import ${package}.users.domain.entities.Credential;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.CredentialRepository;
import ${package}.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
@Lazy
public class AuthUtils {

  // region DEPENDENCIES
  private final BCryptPasswordEncoder passwordEncoder;
  private final CredentialRepository credentialRepository;
  private final UserRepository userRepository;

  // endregion DEPENDENCIES

  // region PASSWORD MANAGEMENT

  /**
   * Encodes the given password
   *
   * @param password Password to encrypt
   * @return Encrypted password
   */
  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  /**
   * Check if the encoding of the given {@code rawPassword} matches with the
   * {@code expectedPassword}.
   *
   * @param rawPassword      Password to encode and check
   * @param expectedPassword Encoded password to check against
   * @return True if the encoding of both passwords are equal
   */
  public boolean doPasswordsMatch(String rawPassword, String expectedPassword) {
    return passwordEncoder.matches(rawPassword, expectedPassword);
  }

  // endregion PASSWORD MANAGEMENT

  // region USER MANAGEMENT

  /**
   * Checks if a user with the given {@code nickname} exists and returns it
   *
   * @param nickname Nickname of the user to be found
   * @return User, if exists
   * @throws UserNotFoundException User with the given nickname does not exist
   */
  public User fetchUserByNickname(String nickname) throws UserNotFoundException {
    // Check credentials exists for the given nickname
    User user = credentialRepository.findByNicknameIgnoreCase(nickname)
        .orElseThrow(() -> new UserNotFoundException(nickname))
        .getUser();

    return user;
  }

  /**
   * Checks if a user with the given {@code userID} exists and returns it
   *
   * @param userID User ID
   * @return User, if exists
   * @throws UserNotFoundException User with the given nickname does not exist
   */
  public User fetchUserByUserId(UUID userID) throws UserNotFoundException {
    return userRepository.findById(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));
  }

  public Credential findUserCredentials(UUID userID) throws UserNotFoundException {
    return credentialRepository.findCredentialByUserID(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));
  }

  // endregion USER MANAGEMENT


}