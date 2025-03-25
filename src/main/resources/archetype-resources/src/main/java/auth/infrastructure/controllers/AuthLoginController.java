package ${package}.auth.infrastructure.controllers;

import ${package}.auth.application.usecases.login.LoginUserUseCase;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.LoginParamsDTO;
import ${package}.auth.infrastructure.dto.outbound.AuthenticatedUserDTO;
import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserNotFoundException;
import java.util.UUID;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ${package}.common.security.SecurityConstants.USER_ID_ATTRIBUTE_NAME;
import static ${package}.common.security.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/login")
public class AuthLoginController {


  // region DEPENDENCIES
  private final LoginUserUseCase loginUseCase;
  private final JwtGenerator jwtGenerator;
  // endregion DEPENDENCIES

  // region I18N KEYS

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS

  // endregion EXCEPTION HANDLERS

  // region ENDPOINTS

  @PostMapping(path = "/password",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public AuthenticatedUserDTO loginWithNicknameAndPassword(
      @Validated @RequestBody LoginParamsDTO params) throws IncorrectLoginException {
    User user = loginUseCase.loginWithNicknameAndPassword(params.getNickname(), params.getPassword());
    String token = generateServiceTokenFromUser(user);
    AuthenticatedUserDTO authenticatedUser = AuthConversor.toAuthenticatedUserDTO(token, user);

    return authenticatedUser;
  }

  @PostMapping(path = "/token",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public AuthenticatedUserDTO loginUsingToken(
      @RequestAttribute(USER_ID_ATTRIBUTE_NAME) UUID userID,
      @RequestAttribute(TOKEN_ATTRIBUTE_NAME) String token) throws UserNotFoundException {
    User user = loginUseCase.loginWithJsonWebToken(userID);
    AuthenticatedUserDTO authenticatedUser = AuthConversor.toAuthenticatedUserDTO(token, user);

    return authenticatedUser;
  }

  // endregion ENDPOINTS

  // region AUXILIAR FUNCTIONS

  /**
   * Genera un JWT para el usuario recibido
   */
  private String generateServiceTokenFromUser(User user) {
    List<RoleNames> userRoleNames = user.getRoles().stream().map(Role::getName).toList();

    JwtData jwtData = new JwtData(user.getUserID(), user.getNickname(), userRoleNames);

    return jwtGenerator.generateJWT(jwtData);
  }

  // endregion AUXILIAR FUNCTIONS
}