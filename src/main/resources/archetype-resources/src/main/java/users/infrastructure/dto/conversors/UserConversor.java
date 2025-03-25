package ${package}.users.infrastructure.dto.conversors;

import ${package}.users.domain.entities.User;
import ${package}.users.infrastructure.dto.outbound.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static ${package}.users.infrastructure.dto.conversors.ContactInfoConversor.toContactInfoDTO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConversor {

  // region to DTO
  public static UserDTO toUserDTO(User entity) {
    ContactInfoDTO contactInfoDTO = toContactInfoDTO(entity.getContactInfo());

    return UserDTO.builder()
        .userID(entity.getUserID())
        .name(entity.getName())
        .surname(entity.getSurname())
        .gender(entity.getGender())
        .nickname(entity.getCredential().getNickname())
        .birthDate(entity.getBirthDate())
        .registeredAt(entity.getRegisteredAt())
        // Other attributes
        .roles(entity.getRoles())
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
