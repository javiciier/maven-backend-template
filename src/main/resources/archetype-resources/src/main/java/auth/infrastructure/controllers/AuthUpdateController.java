package ${package}.auth.infrastructure.controllers;

import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.auth.application.usecases.update.UpdateCredentialsUseCase;
import ${package}.auth.infrastructure.dto.conversors.AuthConversor;
import ${package}.auth.infrastructure.dto.inbound.ChangePasswordParamsDTO;
import ${package}.auth.infrastructure.dto.outbound.AuthenticatedUserDTO;
import ${package}.common.annotations.validations.CheckUserIdentity;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/update")
public class AuthUpdateController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final UpdateCredentialsUseCase updateCredentialsUseCase;
  private final JwtGenerator jwtGenerator;
  private final ServletContext servletContext;
  // endregion DEPENDENCIES

  // region I18N KEYS

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = servletContext.getContextPath() + "/auth/update";

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.PUT, BASE_ENDPOINT+"/*/password").fullyAuthenticated()
    );
  }

  // region ENDPOINTS
  @PutMapping(path = "/{userID}/password",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CheckUserIdentity
  public ApiResponse<Void> updatePassword(@PathVariable("userID") UUID pathUserID,
      @Validated @RequestBody ChangePasswordParamsDTO params)
      throws UserNotFoundException, PasswordsMismatchException {
    updateCredentialsUseCase.updatePassword(pathUserID, params.getOldPassword(), params.getNewPassword());

    return ApiResponseHelper.buildEmptySuccessApiResponse();
  }
  // endregion ENDPOINTS

  // region AUXILIAR METHODS

  // endregion AUXILIAR METHODS
}