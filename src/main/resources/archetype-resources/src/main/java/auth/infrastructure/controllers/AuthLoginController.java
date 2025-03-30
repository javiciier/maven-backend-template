package ${package}.auth.infrastructure.controllers;

import ${package}.auth.application.usecases.login.LoginUserUseCase;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.LoginParamsDTO;
import ${package}.auth.infrastructure.dto.outbound.AuthenticatedUserDTO;
import ${package}.common.config.EndpointSecurityConfigurer;
import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.ApiResponseHelper;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserNotFoundException;
import jakarta.servlet.ServletContext;
import java.util.UUID;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static ${package}.common.security.SecurityConstants.USER_ID_ATTRIBUTE_NAME;
import static ${package}.common.security.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/login")
public class AuthLoginController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final LoginUserUseCase loginUseCase;
  private final JwtGenerator jwtGenerator;
  private final ServletContext servletContext;
  // endregion DEPENDENCIES

  // region I18N KEYS

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = servletContext.getContextPath() + "/auth/login";

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.POST, BASE_ENDPOINT+"/password").anonymous()
        .requestMatchers(HttpMethod.POST, BASE_ENDPOINT+"/token").anonymous()
    );
  }

  // region ENDPOINTS
  @PostMapping(path = "/password",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<AuthenticatedUserDTO> loginWithNicknameAndPassword(
      @Validated @RequestBody LoginParamsDTO params) throws IncorrectLoginException {
    User user = loginUseCase.loginWithNicknameAndPassword(params.getNickname(), params.getPassword());
    String token = generateServiceTokenFromUser(user);
    AuthenticatedUserDTO authenticatedUser = AuthConversor.toAuthenticatedUserDTO(token, user);

    return ApiResponseHelper.buildSuccessApiResponse(authenticatedUser);
  }

  @PostMapping(path = "/token",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<AuthenticatedUserDTO> loginUsingToken(
      @RequestAttribute(USER_ID_ATTRIBUTE_NAME) UUID userID,
      @RequestAttribute(TOKEN_ATTRIBUTE_NAME) String token) throws UserNotFoundException {
    User user = loginUseCase.loginWithJsonWebToken(userID);
    AuthenticatedUserDTO authenticatedUser = AuthConversor.toAuthenticatedUserDTO(token, user);

    return ApiResponseHelper.buildSuccessApiResponse(authenticatedUser);
  }

  // endregion ENDPOINTS

  // region AUXILIAR METHODS

  /**
   * Genera un JWT para el usuario recibido
   */
  private String generateServiceTokenFromUser(User user) {
    List<RoleNames> userRoleNames = user.getRoles().stream().map(Role::getName).toList();

    JwtData jwtData = new JwtData(user.getUserID(), user.getNickname(), userRoleNames);

    return jwtGenerator.generateJWT(jwtData);
  }

  // endregion AUXILIAR METHODS
}