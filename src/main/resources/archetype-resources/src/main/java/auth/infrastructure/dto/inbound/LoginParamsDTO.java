package ${package}.auth.infrastructure.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginParamsDTO(
    @NotBlank
    @Size(min = 1, max = 50)
    String nickname,

    @NotBlank
    @Size(min = 1)
    String password
) {

}
