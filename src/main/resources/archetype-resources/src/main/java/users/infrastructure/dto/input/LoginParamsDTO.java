package ${package}.users.infrastructure.dto.input;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParamsDTO {
    @NotBlank
    @Size(min = 1, max = 50)
    private String nickname;

    @NotBlank
    @Size(min = 1)
    private String password;
}
