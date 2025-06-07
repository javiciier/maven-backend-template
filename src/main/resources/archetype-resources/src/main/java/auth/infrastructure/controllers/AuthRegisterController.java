package ${package}.auth.infrastructure.controllers;

import ${package}.auth.application.usecases.register.RegisterUserUseCase;
import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.RegisterUserParamsDTO;
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
import ${package}.users.domain.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static ${package}.common.api.ApiResponseHelper.buildErrorApiResponse;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/register")
public class AuthRegisterController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final RegisterUserUseCase registerUserUseCase;
  private final JwtGenerator jwtGenerator;
  private final Translator appTranslator;
  // endregion DEPENDENCIES

  // region I18N KEYS
  public static final String USER_ALREADY_EXISTS_EXCEPTION_KEY = "UserAlreadyExistsException";

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS
  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<ErrorApiResponseBody> handleUserAlreadyExistsException(
      PasswordsMismatchException exception, Locale locale) {
    String errorMessage = appTranslator.generateMessage(USER_ALREADY_EXISTS_EXCEPTION_KEY, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = "/auth/register";

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.POST, BASE_ENDPOINT + "/").permitAll()
    );
  }

  // region ENDPOINTS

  @PostMapping(path = "/",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ApiResponse<AuthenticatedUserDTO>> registerWithNicknameAndPassword(
      @Validated @RequestBody RegisterUserParamsDTO params) throws UserAlreadyExistsException {
    // Register user
    User user = registerUserUseCase.register(params);

    // Generate response content
    String token = generateServiceTokenFromUser(user);
    AuthenticatedUserDTO authenticatedUserDTO = AuthConversor.toAuthenticatedUserDTO(token, user);
    ApiResponse<AuthenticatedUserDTO> responseBody = ApiResponseHelper.buildSuccessApiResponse(authenticatedUserDTO);

    // Build response
    URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{userID}")
        .buildAndExpand(user.getUserID())
        .toUri();
    return ResponseEntity
        .created(resourceLocation)
        .contentType(MediaType.APPLICATION_JSON)
        .body(responseBody);
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