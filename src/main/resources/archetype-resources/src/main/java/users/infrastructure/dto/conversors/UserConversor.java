package ${package}.users.infrastructure.dto.conversors;

import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.*;
import ${package}.users.infrastructure.dto.outbound.*;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConversor {

  // region to DTO
  public static UserDTO toUserDTO(User entity) {
    ContactInfoDTO contactInfoDTO = ContactInfoConversor.toContactInfoDTO(entity.getContactInfo());
    List<RoleNames> roles = entity.getRoles().stream().map(Role::getName).sorted().toList();

    return UserDTO.builder()
        // User attributes
        .userID(entity.getUserID())
        .name(entity.getName())
        .surname(entity.getSurname())
        .gender(entity.getGender())
        .nickname(entity.getCredential().getNickname())
        .birthDate(entity.getBirthDate())
        .registeredAt(entity.getRegisteredAt())
        // Other relations attributes
        .roles(roles)
        .contactInfo(contactInfoDTO)
        .build();
  }

  // endregion to

  // region to DTO collection
  // endregion to DTO collection

  // region to entity
  // endregion to entity

  // region to entity collection
  // endregion to entity collection

}
