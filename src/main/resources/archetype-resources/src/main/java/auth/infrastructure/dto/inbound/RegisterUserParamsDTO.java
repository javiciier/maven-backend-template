package ${package}.auth.infrastructure.dto.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import ${package}.common.interfaces.EntityConversor;
import ${package}.users.domain.entities.Gender;
import ${package}.users.domain.entities.User;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterUserParamsDTO(
    // User
    @NotBlank
    @Size(min = 1, max = 50)
    String name,

    @NotBlank
    @Size(min = 1, max = 50)
    String surname,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Gender gender,

    @NotNull
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate birthDate,

    // Credentials
    @NotBlank
    @Size(min = 1, max = 50)
    String nickname,

    @NotBlank
    @Size(min = 1)
    String plainPassword,

    // ContactInfo
    @NotBlank
    @Email
    @Size(min = 1, max = 100)
    String email,

    @NotBlank
    @Size(min = 1, max = 20)
    String phoneNumber
) implements EntityConversor<User> {
  public static class Builder {
    private String name;
    private String surname;
    private Gender gender;
    private LocalDate birthDate;
    private String nickname;
    private String plainPassword;
    private String email;
    private String phoneNumber;

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

    public Builder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
    }

    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public Builder plainPassword(String plainPassword) {
      this.plainPassword = plainPassword;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public RegisterUserParamsDTO build() {
      return new RegisterUserParamsDTO(name, surname, gender, birthDate, nickname, plainPassword, email, phoneNumber);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public User toEntity() {
    return User.builder()
        .name(name)
        .surname(surname)
        .gender(gender)
        .birthDate(birthDate)
        .build();
  }
}
