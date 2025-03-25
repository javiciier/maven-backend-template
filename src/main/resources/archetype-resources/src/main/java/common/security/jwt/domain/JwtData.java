package ${package}.common.security.jwt.domain;

import ${package}.users.domain.entities.roles.RoleNames;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class JwtData {

  private UUID userID;
  private String nickname;
  private List<RoleNames> roles;
}
