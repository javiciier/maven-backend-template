package ${package}.users.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends Exception {

  private String nickname;
  private UUID userID;

  public UserNotFoundException(String nickname) {
    super("User with nickname '%s' not found".formatted(nickname));
    this.nickname = nickname;
  }

  public UserNotFoundException(UUID userID) {
    super("User with ID '%s' not found".formatted(userID));
    this.userID = userID;
  }
}
