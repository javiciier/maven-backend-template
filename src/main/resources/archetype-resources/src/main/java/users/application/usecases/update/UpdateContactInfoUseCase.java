package ${package}.users.application.usecases.update;

import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.ContactInfoRepository;
import ${package}.users.domain.repositories.UserRepository;
import ${package}.users.infrastructure.dto.inbound.UpdateContactInfoParamsDTO;
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
public class UpdateContactInfoUseCase {

  // region DEPENDENCIES
  private final ContactInfoRepository contactInfoRepository;
  private final UserRepository userRepository;

  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Updates the contact information of the user with the given {@code userID}
   *
   * @param userID       User ID
   * @param updateParams New contact information to be updated
   * @return User with the updated contact information
   * @throws UserNotFoundException No user was found
   */
  public User updateContactInfo(UUID userID, UpdateContactInfoParamsDTO updateParams)
      throws UserNotFoundException {
    log.debug("Trying to update contact information for user with ID '{}'", userID);

    // Get the user contact information
    User user = userRepository.findUserWithCredentialsAndContactInfoAndRoleAssignmentsAndRole(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));
    ContactInfo contactInfo = user.getContactInfo();

    boolean hasDataToUpdate = shouldUpdateContactInfo(contactInfo, updateParams);
    if (!hasDataToUpdate) {
      return user;
    }

    contactInfoRepository.save(contactInfo);
    log.debug("User with ID '{}' has updated his contact info succesfuly", userID);

    return user;
  }

  // endregion USE CASES

  // region AUXILIAR METHODS
  private boolean shouldUpdateContactInfo(ContactInfo currentInfo, UpdateContactInfoParamsDTO dto) {
    boolean shouldUpdate = false;

    if (hasChanged(currentInfo.getEmail(), dto.newEmail())) {
      // TODO Validate and confirm email
      currentInfo.setEmail(dto.newEmail());
      shouldUpdate = true;
    }

    if (hasChanged(currentInfo.getPhoneNumber(), dto.newMobilePhone())) {
      // TODO Validate and confirm phone
      currentInfo.setPhoneNumber(dto.newMobilePhone());
      shouldUpdate = true;
    }

    return shouldUpdate;
  }

  private <T extends Object> boolean hasChanged(T expected, T received) {
    return !expected.equals(received);
  }
  // endregion AUXILIAR METHODS

}
