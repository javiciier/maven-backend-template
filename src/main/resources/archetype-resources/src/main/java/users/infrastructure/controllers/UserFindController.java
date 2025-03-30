package ${package}.users.infrastructure.controllers;

import ${package}.common.config.EndpointSecurityConfigurer;
import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.ApiResponseHelper;
import ${package}.users.application.usecases.find.FindUserUseCase;
import ${package}.users.domain.entities.*;
import ${package}.users.domain.entities.roles.*;
import ${package}.users.domain.exceptions.UserNotFoundException;
import ${package}.users.infrastructure.dto.conversors.UserConversor;
import ${package}.users.infrastructure.dto.outbound.*;
import jakarta.servlet.ServletContext;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@Lazy
@RequestMapping("/users/find")
public class UserFindController implements EndpointSecurityConfigurer {

  // region DEPENDENCIES
  private final FindUserUseCase findUserUseCase;
  private final ServletContext servletContext;
  // endregion DEPENDENCIES

  // region I18N KEYS

  // endregion I18N KEYS

  // region EXCEPTION HANDLERS

  // endregion EXCEPTION HANDLERS

  @Override
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception {
    final String BASE_ENDPOINT = servletContext.getContextPath() + "/users/find";

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.GET, BASE_ENDPOINT+"/").anonymous()
    );
  }

  // region ENDPOINTS
  @GetMapping(path = "/",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<UserDTO> findUserById(@RequestParam("userID") UUID userID) throws UserNotFoundException {
    User user = findUserUseCase.findByUserId(userID);
    UserDTO userDTO = UserConversor.toUserDTO(user);

    return ApiResponseHelper.buildSuccessApiResponse(userDTO);
  }

  // endregion ENDPOINTS

  // region AUXILIAR FUNCTIONS

  // endregion AUXILIAR FUNCTIONS
}