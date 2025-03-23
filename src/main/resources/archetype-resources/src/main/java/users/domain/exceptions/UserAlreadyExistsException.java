package ${package}.users.domain.exceptions;

import ${package}.users.domain.entities.User;

public class UserAlreadyExistsException extends Exception {
    public static final String ERROR_MESSAGE = "A user with nickname '%s' already exists";

    private User user;
    private String nickname;

    public UserAlreadyExistsException(User user) {
        super(ERROR_MESSAGE.formatted(user.getNickname()));
        this.user = user;
    }

    public UserAlreadyExistsException(String nickname) {
        super(ERROR_MESSAGE.formatted(nickname));
        this.nickname = nickname;
    }
}
