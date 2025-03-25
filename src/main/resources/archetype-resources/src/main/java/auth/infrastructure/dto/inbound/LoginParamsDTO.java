package ${package}.auth.infrastructure.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginParamsDTO {

  @NotBlank
  @Size(min = 1, max = 50)
  private final String nickname;

  @NotBlank
  @Size(min = 1)
  private final String password;
}
