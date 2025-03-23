package ${package}.users.infrastructure.dto.conversors;

import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.infrastructure.dto.output.ContactInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactInfoConversor {

  /* ******************** Convertir a DTO ******************** */
  public static ContactInfoDTO toContactInfoDTO(ContactInfo entity) {
    return ContactInfoDTO.builder()
        .contactInfoID(entity.getContactInfoID())
        .email(entity.getEmail())
        .isEmailVerified(entity.getIsEmailVerified())
        .phoneNumber(entity.getPhoneNumber())
        .isPhoneNumberVerified(entity.getIsPhoneNumberVerified())
        .build();
  }

}
