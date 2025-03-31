package ${package}.auth.infrastructure.dto.outbound;

import ${package}.users.infrastructure.dto.outbound.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import static ${package}.common.security.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Data
@RequiredArgsConstructor
@Builder
public final class AuthenticatedUserDTO {
  @JsonProperty(value = TOKEN_ATTRIBUTE_NAME)
  private final String serviceToken;

  @JsonProperty(value = "user")
  private final UserDTO userDTO;
}
