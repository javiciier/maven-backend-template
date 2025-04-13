package ${package}.users.infrastructure.dto.inbound;

import jakarta.validation.constraints.Email;

public record UpdateContactInfoParamsDTO(
    @Email
    String newEmail,

    String newMobilePhone
) {
  public static class Builder {
    private String newEmail;
    private String newMobilePhone;

    public Builder newEmail(String newEmail) {
      this.newEmail = newEmail;
      return this;
    }

    public Builder newMobilePhone(String newMobilePhone) {
      this.newMobilePhone = newMobilePhone;
      return this;
    }

    public UpdateContactInfoParamsDTO build() {
      return new UpdateContactInfoParamsDTO(newEmail, newMobilePhone);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
