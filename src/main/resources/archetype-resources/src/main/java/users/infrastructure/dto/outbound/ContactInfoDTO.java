package ${package}.users.infrastructure.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record ContactInfoDTO(
    Long contactInfoID,

    @NotBlank
    @Email
    String email,

    @NotNull
    @JsonProperty(value = "isEmailVerified")
    Boolean isEmailVerified,

    String phoneNumber,

    @NotNull
    @JsonProperty(value = "isPhoneNumberVerified")
    Boolean isPhoneNumberVerified
) {
  public static class Builder {
    private Long contactInfoID;
    private String email;
    private Boolean isEmailVerified;
    private String phoneNumber;
    private Boolean isPhoneNumberVerified;

    public Builder contactInfoID(Long contactInfoID) {
      this.contactInfoID = contactInfoID;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder isEmailVerified(Boolean isEmailVerified) {
      this.isEmailVerified = isEmailVerified;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Builder isPhoneNumberVerified(Boolean isPhoneNumberVerified) {
      this.isPhoneNumberVerified = isPhoneNumberVerified;
      return this;
    }

    public ContactInfoDTO build() {
      return new ContactInfoDTO(contactInfoID, email, isEmailVerified, phoneNumber, isPhoneNumberVerified);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
