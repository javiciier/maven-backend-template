package ${package}.users.application.usecases.find;

import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
@Lazy
@Transactional(readOnly = true)
public class FindUserUseCase {

  // region DEPENDENCIES
  private final UserRepository userRepository;
  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Finds a {@link User} with the given {@code userID}.
   *
   * @param userID ID of the user to find
   * @return User found
   * @throws UserNotFoundException User was not found
   */
  public User findByUserId(UUID userID) throws UserNotFoundException {
    return userRepository
        .findUserWithCredentialsAndContactInfoAndRoleAssignmentsAndRole(userID)
        .orElseThrow(() -> new UserNotFoundException(userID));
  }

  // endregion USE CASES
}
