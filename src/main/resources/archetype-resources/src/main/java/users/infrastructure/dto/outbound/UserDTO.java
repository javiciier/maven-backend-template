package ${package}.users.infrastructure.dto.outbound;

import ${package}.users.domain.entities.*;
import ${package}.users.domain.entities.roles.RoleNames;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDTO(
    UUID userID,

    @NotBlank
    @Size(min = 1, max = 50)
    String name,

    @NotNull
    @Size(min = 1, max = 50)
    String surname,

    @NotNull
    Gender gender,

    @NotBlank
    @Size(min = 1, max = 30)
    String nickname,

    @PastOrPresent
    LocalDate birthDate,

    @PastOrPresent
    LocalDateTime registeredAt,

    List<RoleNames> roles,

    ContactInfoDTO contactInfo
) {

  // Builder manual
  public static class Builder {
    private UUID userID;
    private String name;
    private String surname;
    private Gender gender;
    private String nickname;
    private LocalDate birthDate;
    private LocalDateTime registeredAt;
    private List<RoleNames> roles;
    private ContactInfoDTO contactInfo;

    public Builder userID(UUID userID) {
      this.userID = userID;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder gender(Gender gender) {
      this.gender = gender;
      return this;
    }

    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public Builder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
    }

    public Builder registeredAt(LocalDateTime registeredAt) {
      this.registeredAt = registeredAt;
      return this;
    }

    public Builder roles(List<RoleNames> roles) {
      this.roles = roles;
      return this;
    }

    public Builder contactInfo(ContactInfoDTO contactInfo) {
      this.contactInfo = contactInfo;
      return this;
    }

    public UserDTO build() {
      return new UserDTO(userID, name, surname, gender, nickname, birthDate, registeredAt, roles, contactInfo);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}

