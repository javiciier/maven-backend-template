package $package.users.domain.exceptions;

import $package.common.exception.EntityAlreadyExistsException;
import $package.users.domain.User;
import jakarta.validation.constraints.NotNull;

public class UserAlreadyExistsException extends EntityAlreadyExistsException {
    public static final String USER_CLASSNAME = User.class.getSimpleName();

    public UserAlreadyExistsException(@NotNull Object key) {
        super(USER_CLASSNAME, key);
    }
}
