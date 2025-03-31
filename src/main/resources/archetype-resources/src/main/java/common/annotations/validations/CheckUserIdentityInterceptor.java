package ${package}.common.annotations.validations;

import com.fasterxml.jackson.databind.ObjectMapper;
import ${package}.common.Translator;
import ${package}.common.annotations.validations.CheckUserIdentity;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.ApiResponseHelper;
import ${package}.common.api.error.ErrorApiResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Optional;
import java.util.Map;
import java.util.Locale;
import java.util.UUID;

import static ${package}.common.CommonControllerAdvice.INVALID_ARGUMENT_KEY;
import static ${package}.common.CommonControllerAdvice.PERMISSION_EXCEPTION_KEY;
import static ${package}.common.security.SecurityConstants.USER_ID_ATTRIBUTE_NAME;

/**
 * Implements the logic related to {@link CheckUserIdentity} annotation.
 */
@Component
@RequiredArgsConstructor
public class CheckUserIdentityInterceptor implements HandlerInterceptor {
  // region DEPENDENCIES
  private ObjectMapper objectMapper;
  private Locale locale;
  // endregion DEPENDENCIES

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // Check if this interceptor can handle the annotation
    if (!canHandleAnnotation(handler)) {
      return true;
    }

    // Check request contains user ID (mandatory)
    Optional<UUID> requestID = extractId(request.getAttribute(USER_ID_ATTRIBUTE_NAME));
    if (requestID.isEmpty()) {
      String errorMessage = Translator.generateMessage(PERMISSION_EXCEPTION_KEY, locale);
      failResponseWithError(response, HttpStatus.FORBIDDEN, errorMessage);
      return false;
    }

    // Check request URI contains user ID (optional)
    Optional<UUID> pathID = extractIdFromPath(request);
    if (pathID.isEmpty()) {
      String errorMessage = Translator.generateMessage(INVALID_ARGUMENT_KEY, locale);
      failResponseWithError(response, HttpStatus.BAD_REQUEST, errorMessage);
      return false;
    }

    // Given both ID, check they match
    if (!requestID.equals(pathID)) {
      String errorMessage = Translator.generateMessage(PERMISSION_EXCEPTION_KEY, locale);
      failResponseWithError(response, HttpStatus.FORBIDDEN, errorMessage);
      return false;
    }

    return true;
  }

  // region AUXILIAR METHODS
  private static boolean canHandleAnnotation(Object handler) {
    return (handler instanceof HandlerMethod hm) && (hm.hasMethodAnnotation(CheckUserIdentity.class));
  }

  private Optional<UUID> extractId(Object value) {
    try {
      return Optional.ofNullable(value)
          .map(Object::toString)
          .map(UUID::fromString);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private Optional<UUID> extractIdFromPath(HttpServletRequest request) {
    return Optional.ofNullable(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(vars -> vars.get(USER_ID_ATTRIBUTE_NAME))
        .flatMap(this::extractId);
  }

  private void failResponseWithError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ApiResponse<ErrorApiResponseBody> errorResponse = ApiResponseHelper.buildErrorApiResponse(status, message);
    String errorResponseAsJson = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(errorResponseAsJson);
  }

  // endregion AUXILIAR METHODS
}
