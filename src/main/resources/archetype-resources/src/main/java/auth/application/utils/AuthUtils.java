package ${package}.auth.application.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
@Lazy
public class AuthUtils {

  // region DEPENDENCIES
  private final BCryptPasswordEncoder passwordEncoder;

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

  // endregion USER MANAGEMENT


}