package ${package}.auth.infrastructure.dto.conversors;

import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
import ${package}.auth.infrastructure.dto.outbound.AuthenticatedUserDTO;
import ${package}.users.domain.entities.User;
import ${package}.users.infrastructure.dto.conversors.UserConversor;
import ${package}.users.infrastructure.dto.outbound.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConversor {

  // region to DTO

  public static AuthenticatedUserDTO toAuthenticatedUserDTO(String token, User entity) {
    UserDTO userDTO = UserConversor.toUserDTO(entity);

    return new AuthenticatedUserDTO(token, userDTO);
  }

  // endregion to DTO

  // region to DTO collection
  // endregion to DTO collection


  // region to entity

  public static User fromRegisterUserParamsDTO(RegisterUserParamsDTO dto) {
    return dto.toEntity();
  }

  // endregion to entity

  // region to entity collection
  // endregion to entity collection


}
