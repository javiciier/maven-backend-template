package ${package}.common.security.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtDataFields {
  USER_ID("userID"),
  NICKNAME("nickname"),
  ROLES("roles");

  private String value;
}
