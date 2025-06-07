package ${package}.users.domain.exceptions;

import ${package}.users.domain.entities.User;

public class UserAlreadyExistsException extends Exception {

  public static final String ERROR_MESSAGE = "A user with nickname '%s' already exists";

  private String nickname;

  public UserAlreadyExistsException(User user) {
    this(user.getNickname());
  }

  public UserAlreadyExistsException(String nickname) {
    super(ERROR_MESSAGE.formatted(nickname));
    this.nickname = nickname;
  }
}
