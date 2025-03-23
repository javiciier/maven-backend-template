package users.application.usecases.find;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;{package}.users.domain.exceptions .*;

@Log4j2
@RequiredArgsConstructor
@Service
@Lazy
@Transactional
public class FindUserUseCase {

  // region DEPENDENCIES
  private final AuthUtils authUtils;

  // endregion DEPENDENCIES

  // region USE CASES

  /**
   * Find a user with the given {@code userID}.
   *
   * @param userID User ID
   * @return Found user, if existent
   * @throws UserNotFoundException No user was found
   */
  public User findUserByID(UUID userID) throws UserNotFoundException {
    return authUtils.fetchUserByUserId(userID);
  }

  // endregion USE CASES
}
