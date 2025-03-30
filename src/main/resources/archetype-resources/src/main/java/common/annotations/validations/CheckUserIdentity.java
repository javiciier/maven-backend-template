package ${package}.common.annotations.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to ensure that the authenticated user of a request matches with the user affected by the request.
 * <p>
 *   Annotate a endpoint method to ensure that the userID inside the request attribute and the userID
 *   in the request URI path affect the same user.
 *   A 403 FORBIDDEN response will be returned if users do not match
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserIdentity {

}
