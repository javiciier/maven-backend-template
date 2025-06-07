package ${package}.common;

import ${package}.common.api.ApiResponse;
import ${package}.common.api.error.ApiValidationErrorDetails;
import ${package}.common.api.error.ErrorApiResponseBody;
import ${package}.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static ${package}.common.api.ApiResponseHelper.buildErrorApiResponse;

@ControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {
  // region DEPENDENCIES
  private final Translator appTranslator;

  // endregion DEPENDENCIES

  // region EXCEPTION KEYS
  public static final String METHOD_ARGUMENT_NOT_VALID_KEY = "MethodArgumentNotValidException";
  public static final String MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION_KEY = "MissingServletRequestParameterException";
  public static final String VALIDATION_KEY = "ValidationException";
  public static final String INVALID_ARGUMENT_KEY = "InvalidArgumentException";
  public static final String MISSING_MANDATORY_VALUE_KEY = "MissingMandatoryValueException";
  public static final String PERMISSION_EXCEPTION_KEY = "PermissionException";

  // endregion EXCEPTION KEYS

  // region EXCEPTION HANDLERS
  // region Spring exceptions

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception, Locale locale) {
    List<ApiValidationErrorDetails> errorsList = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map((field -> new ApiValidationErrorDetails(
            field.getObjectName(),
            field.getField(),
            field.getRejectedValue(),
            field.getDefaultMessage()))
        ).toList();

    String errorMessage = appTranslator.generateMessage(METHOD_ARGUMENT_NOT_VALID_KEY, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage, null, errorsList);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception, Locale locale) {
    Object[] args = {exception.getParameterName(), exception.getParameterType()};
    Locale es_locale = new Locale("es");
    String errorMessage = appTranslator.generateMessage(MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION_KEY, args, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  @ExceptionHandler(InvalidArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handleInvalidArgumentException(
      InvalidArgumentException exception, Locale locale) {
    Object[] args = {exception.getValue(), exception.getObjectName(), exception.getField()};
    String errorMessage = appTranslator.generateMessage(INVALID_ARGUMENT_KEY, args, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  @ExceptionHandler(MissingMandatoryValueException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handleMissingMandatoryValueException(
      MissingMandatoryValueException exception, Locale locale) {
    Object[] args = {exception.getField()};
    String errorMessage = appTranslator.generateMessage(MISSING_MANDATORY_VALUE_KEY, args, locale);

    return buildErrorApiResponse(HttpStatus.BAD_REQUEST, errorMessage);
  }

  // endregion Spring exceptions

  @ExceptionHandler(PermissionException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ApiResponse<ErrorApiResponseBody> handlePermissionException(PermissionException exception, Locale locale) {
    String errorMessage = appTranslator.generateMessage(PERMISSION_EXCEPTION_KEY, locale);

    return buildErrorApiResponse(HttpStatus.FORBIDDEN, errorMessage);
  }

  // endregion EXCEPTION HANDLERS

  // region AUXILIAR METHODS

  // endregion AUXILIAR METHODS

}
