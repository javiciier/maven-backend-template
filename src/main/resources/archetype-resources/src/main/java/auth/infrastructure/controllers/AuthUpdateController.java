package ${package}.auth.infrastructure.controllers;

import ${package}.auth.application.usecases.update.UpdateCredentialsUseCase;
import ${package}.auth.domain.exceptions.PasswordsMismatchException;
import ${package}.auth.infrastructure.dto.inbound.ChangePasswordParamsDTO;
import ${package}.common.annotations.validations.CheckUserIdentity;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.ApiResponseHelper;
import ${package}.common.config.EndpointSecurityConfigurer;
import ${package}.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/auth/update")
public class AuthUpdateController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final UpdateCredentialsUseCase updateCredentialsUseCase;

  // endregion DEPENDENCIES

  // region I18N KEYS

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = "/auth/update";

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
    updateCredentialsUseCase.updatePassword(pathUserID, params.oldPassword(), params.newPassword());

    return ApiResponseHelper.buildEmptySuccessApiResponse();
  }
  // endregion ENDPOINTS

  // region AUXILIAR METHODS

  // endregion AUXILIAR METHODS
}