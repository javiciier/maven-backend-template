package ${package}.common.security.jwt.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class JwtData {

  private UUID userID;
  private String nickname;
  private List<String> roles;
}
