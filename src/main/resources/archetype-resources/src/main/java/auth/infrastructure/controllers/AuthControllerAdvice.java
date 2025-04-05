package ${package}.auth.infrastructure.controllers;

import ${package}.auth.domain.exceptions.*;
import ${package}.common.Translator;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.error.ErrorApiResponseBody;
import ${package}.common.exception.*;
import ${package}.users.domain.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static ${package}.common.api.ApiResponseHelper.buildErrorApiResponse;

@ControllerAdvice(basePackages = "${package}.auth.infrastructure.controllers")
@RequiredArgsConstructor
public class AuthControllerAdvice {
  // region DEPENDENCIES
  private final Translator appTranslator;

  // endregion DEPENDENCIES

  // region EXCEPTION KEYS
  public static final String USER_NOT_FOUND_EXCEPTION_KEY = "UserNotFoundException";
  public static final String PASSWORDS_MISMATCH_KEY = "PasswordsMismatchException";

  // endregion EXCEPTION KEYS

  // region EXCEPTION HANDLERS
  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handleUserNotFoundException(
      UserNotFoundException exception, Locale locale) {
    String errorMessage = appTranslator.generateMessage(USER_NOT_FOUND_EXCEPTION_KEY, locale);

    return buildErrorApiResponse(HttpStatus.NOT_FOUND, errorMessage);
  }

  @ExceptionHandler(PasswordsMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handlePasswordsMismatchException(
      PasswordsMismatchException exception, Locale locale) {
    String errorMessage = appTranslator.generateMessage(PASSWORDS_MISMATCH_KEY, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  // endregion EXCEPTION HANDLERS

}
