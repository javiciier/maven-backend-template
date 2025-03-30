package ${package}.users.application.usecases.find;

import ${package}.auth.application.utils.AuthUtils;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.exceptions.UserNotFoundException;
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
@Transactional(readOnly = true)
public class FindUserUseCase {

  // region DEPENDENCIES
  private final AuthUtils authUtils;

  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Finds a {@link User} with the given {@code userID}
   * @param userID ID of the user to find
   * @return User found
   * @throws UserNotFoundException User was not found
   */
  public User findByUserId(UUID userID) throws UserNotFoundException {
    return authUtils.fetchUserByUserId(userID);
  }

  // endregion USE CASES
}
