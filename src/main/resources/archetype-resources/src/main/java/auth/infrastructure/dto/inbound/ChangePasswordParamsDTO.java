package ${package}.auth.infrastructure.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@RequiredArgsConstructor
public class ChangePasswordParamsDTO {

  @NotBlank
  @Size(min = 1)
  private final String oldPassword;

  @NotBlank
  @Size(min = 1)
  private final String newPassword;
}
