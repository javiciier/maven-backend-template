package $package.common.api;

import $package.common.api.ApiResponse;
import $package.common.api.error.ApiErrorDetails;
import $package.common.api.error.ErrorApiResponseBody;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseHelper {

    // region SUCCESS

    /**
     * Builds a successful response with empty body
     */
    public static ApiResponse<Void> buildEmptySuccessApiResponse() {
        return createResponse(true, null);
    }

    /**
     * Builds a successful response with the given content in the body
     * @param content response content
     * @param <T> Type of the response content
     */
    public static <T> ApiResponse<T> buildSuccessApiResponse(T content) {
        return createResponse(true, content);
    }

    // endregion SUCCESS

    // region ERRORS

    /**
     * Builds an error response with the given HTTP status and message
     * @param status HTTP Status that identifies the error
     * @param message Cause of the error
     */
    public static ApiResponse<ErrorApiResponseBody> buildErrorApiResponse(HttpStatus status, String message) {
        return buildErrorApiResponse(status, message, null);
    }

    /**
     * Builds an error response with the given HTTP status, a message and the exception cause
     * @param status HTTP Status that identifies the error
     * @param message Cause of the error
     * @param exception Exception that caused the error
     */
    public static ApiResponse<ErrorApiResponseBody> buildErrorApiResponse(HttpStatus status, String message, Exception exception) {
        ErrorApiResponseBody body = generateErrorResponseBody(status, message, exception);

        return createResponse(false, body);
    }

    /**
     * Builds an error response with the given HTTP status, a message, the exception cause and a list of the error details
     * @param status HTTP Status that identifies the error
     * @param message Cause of the error
     * @param exception Exception that caused the error
     * @param errorDetails List of details that explain the cause of the error response
     */
    public static ApiResponse<ErrorApiResponseBody> buildErrorApiResponse(HttpStatus status, String message, Exception exception, List<? extends ApiErrorDetails> errorDetails) {
        ErrorApiResponseBody body = generateErrorResponseBody(status, message, exception, errorDetails);

        return createResponse(false, body);
    }

    // endregion ERRORS

    // region auxiliar methods

    private static <T> ApiResponse<T> createResponse(boolean success, T body) {
        return ApiResponse.<T>builder()
                .success(success)
                .data(body)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private static <E extends Exception> ErrorApiResponseBody generateErrorResponseBody(HttpStatus status, String message, E exception) {
        return generateErrorResponseBody(status, message, exception, null);
    }

    private static <E extends ApiErrorDetails> ErrorApiResponseBody generateErrorResponseBody(HttpStatus status, String message, List<E> errors) {
        return generateErrorResponseBody(status, message, null, errors);
    }

    private static <E extends Exception> ErrorApiResponseBody generateErrorResponseBody(HttpStatus status, String message, E exception, List<? extends ApiErrorDetails> errorDetailsList) {
        String debugMessage = (exception != null) ? exception.getLocalizedMessage() : null;
        boolean hasErrors = (errorDetailsList != null) && (!errorDetailsList.isEmpty());
        List<? extends ApiErrorDetails> errorDetails = hasErrors ? errorDetailsList : null;

        return ErrorApiResponseBody.builder()
                .status(status.name())
                .statusCode(status.value())
                .message(message)
                .debugMessage(debugMessage)
                .errors(errorDetails)
                .build();
    }

    // endregion auxiliar methods

}
