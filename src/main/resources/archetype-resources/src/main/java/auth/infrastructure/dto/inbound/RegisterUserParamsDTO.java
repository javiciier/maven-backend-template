package ${package}.auth.infrastructure.dto.inbound;

import ${package}.common.interfaces.EntityConversor;
import ${package}.users.domain.entities.Gender;
import ${package}.users.domain.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserParamsDTO implements EntityConversor<User> {

  // User
  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  @NotBlank
  @Size(min = 1, max = 50)
  private String surname;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Gender gender;

  @NotNull
  @PastOrPresent
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDate birthDate;

  // Credentials
  @NotBlank
  @Size(min = 1, max = 50)
  private String nickname;

  @NotBlank
  @Size(min = 1)
  private String rawPassword;

  // ContactInfo
  @NotBlank
  @Email
  @Size(min = 1, max = 100)
  private String email;

  @NotBlank
  @Size(min = 1, max = 20)
  private String phoneNumber;

  @Override
  public User toEntity() {
    return User.builder()
        .name(this.getName())
        .surname(this.getSurname())
        .gender(this.getGender())
        .birthDate(this.getBirthDate())
        .build();
  }
}
