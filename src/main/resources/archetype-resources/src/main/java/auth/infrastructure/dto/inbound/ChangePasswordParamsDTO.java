package ${package}.auth.infrastructure.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordParamsDTO(
    @NotBlank
    @Size(min = 1)
    String oldPassword,

    @NotBlank
    @Size(min = 1)
    String newPassword
) {

}
