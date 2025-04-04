package ${package}.users.infrastructure.dto.inbound;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContactInfoParamsDTO {

  @Email
  private String newEmail;

  private String newMobilePhone;
}
