package ${package}.auth.infrastructure.controllers;

import ${package}.common.Translator;
import ${package}.common.api.ApiResponse;
import ${package}.common.api.error.ApiValidationErrorDetails;
import ${package}.common.api.error.ErrorApiResponseBody;
import ${package}.common.exception.*;
import ${package}.users.domain.exceptions.*;
import ${package}.auth.domain.exceptions.*;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Locale;

import static ${package}.common.api.ApiResponseHelper.buildErrorApiResponse;

@ControllerAdvice(basePackages = "${package}.auth.infrastructure.controllers")
@NoArgsConstructor
public class AuthControllerAdvice {

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
    String errorMessage = Translator.generateMessage(USER_NOT_FOUND_EXCEPTION_KEY, locale);

    return buildErrorApiResponse(HttpStatus.NOT_FOUND, errorMessage);
  }

  @ExceptionHandler(PasswordsMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handlePasswordsMismatchException(
      PasswordsMismatchException exception, Locale locale) {
    String errorMessage = Translator.generateMessage(PASSWORDS_MISMATCH_KEY, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  // endregion EXCEPTION HANDLERS

}
