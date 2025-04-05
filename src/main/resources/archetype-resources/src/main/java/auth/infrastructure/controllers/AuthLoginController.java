package ${package}.auth.infrastructure.controllers;

import ${package}.auth.application.usecases.login.LoginUserUseCase;
import ${package}.auth.domain.exceptions.IncorrectLoginException;
import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.LoginParamsDTO;
import ${package}.auth.infrastructure.dto.outbound.AuthenticatedUserDTO;
import ${package}.common.Translator;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.ApiResponseHelper;
import ${package}.common.api.error.ErrorApiResponseBody;
import ${package}.common.config.EndpointSecurityConfigurer;
import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.users.domain.entities.User;
import ${package}.users.domain.entities.roles.Role;
import ${package}.users.domain.entities.roles.RoleNames;
import ${package}.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static ${package}.common.api.ApiResponseHelper.buildErrorApiResponse;
import static ${package}.common.security.SecurityConstants.TOKEN_ATTRIBUTE_NAME;
import static ${package}.common.security.SecurityConstants.USER_ID_ATTRIBUTE_NAME;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/login")
public class AuthLoginController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final LoginUserUseCase loginUseCase;
  private final JwtGenerator jwtGenerator;
  private final Translator appTranslator;
  // endregion DEPENDENCIES

  // region I18N KEYS
  public static final String INCORRECT_LOGIN_EXCEPTION_KEY = "IncorrectLoginException";
  // endregion I18N KEYS

  // region EXCEPTION HANDLERS
  @ExceptionHandler(IncorrectLoginException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<ErrorApiResponseBody> handleIncorrectLoginException(
      PasswordsMismatchException exception, Locale locale) {
    String errorMessage = appTranslator.generateMessage(INCORRECT_LOGIN_EXCEPTION_KEY, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = "/auth/login";

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.POST, BASE_ENDPOINT + "/password").permitAll()
        .requestMatchers(HttpMethod.POST, BASE_ENDPOINT + "/token").permitAll()
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
   * Generate a new JWT for the given user
   */
  private String generateServiceTokenFromUser(User user) {
    List<RoleNames> userRoleNames = user.getRoles().stream().map(Role::getName).toList();

    JwtData jwtData = new JwtData(user.getUserID(), user.getNickname(), userRoleNames);

    return jwtGenerator.generateJWT(jwtData);
  }

  // endregion AUXILIAR METHODS
}