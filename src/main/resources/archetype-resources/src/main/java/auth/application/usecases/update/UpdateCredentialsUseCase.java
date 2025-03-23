package ${package}.application.usecases.update;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.users.domain.entities.Credential;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
@Lazy
@Transactional
public class UpdateCredentialsUseCase {

  // region DEPENDENCIES
  private final CredentialRepository credentialRepository;
  private final AuthUtils authUtils;

  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Update the password for the user with the given {@code userID}
   *
   * @param userID      User ID
   * @param oldPassword Old password in plain text
   * @param newPassword New password in plain text
   * @throws UserNotFoundException        User with the given ID does not exist
   * @throws PasswordsDoNotMatchException Old password does not match with the current user
   *                                      password
   */
  public void updatePassword(UUID userID, String plainOldPassword, String plainNewPassword)
      throws UserNotFoundException, PasswordsMismatchException {
    log.info("Trying to update password for user with ID '{}'", userID);

    // Get the user credentials
    Credential credential = credentialRepository.findCredentialByUserID(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));

    // Check old and current passwords match
    String currentPassword = credential.getPasswordHash();
    if (!authUtils.doPasswordsMatch(plainOldPassword, currentPassword)) {
      throw new PasswordsMismatchException();
    }

    // Encript and update user password
    String newPasswordHashed = authUtils.encryptPassword(plainNewPassword);
    credential.setPasswordHash(newPasswordHashed);

    log.info("User with ID '{}' password updated sucessfuly", userID);
  }

  // endregion USE CASES
}
