package $package.users.domain.exceptions;

import $package.common.exception.EntityNotFoundException;
import $package.users.domain.User;

public class UserNotFoundException extends EntityNotFoundException {
    public static final String USER_CLASSNAME = User.class.getSimpleName();

    public UserNotFoundException(Object key) {
        super(USER_CLASSNAME, key);
    }
}
