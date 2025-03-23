package ${package}.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

  // region HTTP
  public static final String PREFIX_BEARER_TOKEN = "Bearer ";

  // endregion HTTP

  // region AUTHENTICATION
  public static final String TOKEN_ATTRIBUTE_NAME = "service_token";
  public static final String USER_ID_ATTRIBUTE_NAME = "userID";
  public static final String ROLE_ATTRIBUTE_NAME = "ROLE_";

  // endregion AUTHENTICATION
}
