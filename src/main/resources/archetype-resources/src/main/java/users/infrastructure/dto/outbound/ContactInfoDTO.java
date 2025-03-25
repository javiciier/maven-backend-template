package ${package}.users.infrastructure.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ContactInfoDTO {

  private final Long contactInfoID;

  @NotBlank
  @Email
  private String email;

  @NotNull
  @JsonProperty(value = "isEmailVerified")
  private Boolean isEmailVerified;

  private String phoneNumber;

  @NotNull
  @JsonProperty(value = "isPhoneNumberVerified")
  private Boolean isPhoneNumberVerified;

}
