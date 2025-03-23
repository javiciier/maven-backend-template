package ${package}.users.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends Exception {

  private String nickname;
  private UUID id;

  public UserNotFoundException(String nickname) {
    super("User with nickname '%s' not found".formatted(nickname));
    this.nickname = nickname;
  }

  public UserNotFoundException(UUID id) {
    super("User with ID '%s' not found".formatted(id));
    this.id = id;
  }
}
