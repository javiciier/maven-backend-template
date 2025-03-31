package ${package}.users.infrastructure.dto.conversors;

import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.infrastructure.dto.outbound.ContactInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContactInfoConversor {

  // region to DTO
  public static ContactInfoDTO toContactInfoDTO(ContactInfo entity) {
    return ContactInfoDTO.builder()
        .email(entity.getEmail())
        .isEmailVerified(entity.getIsEmailVerified())
        .phoneNumber(entity.getPhoneNumber())
        .isPhoneNumberVerified(entity.getIsPhoneNumberVerified())
        .build();
  }

  // endregion to DTO


}
