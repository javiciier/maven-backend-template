package ${package}.utils;

import static ${package}.common.CommonControllerAdvice.PERMISSION_EXCEPTION_KEY;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ${package}.common.Translator;
import ${package}.common.exception.PermissionException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Utility class to do test a response returned by an endpoint on a test.
 */
@RequiredArgsConstructor
public final class ApiResponseTestUtils {

  // region DEPENDENCIES
  private final Translator appTranslator;

  // endregion DEPENDENCIES

  // region UTILS
  // region SUCCESS RESPONSES

  /**
   * Asserts that the given response body {@code testResult} matches with a succesful response of
   * type 204 No Content and the data field is empty.
   */
  public static void assertResponseIsSuccessNoContent(ResultActions testResult) throws Exception {
    String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    testResult.andExpectAll(
        status().isNoContent(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("$.success", is(true)),
        jsonPath("$.timestamp", lessThan(now)),
        jsonPath("$.data", nullValue())
    );
  }

  /**
   * Asserts that the given response body {@code testResult} matches with a succesful response of
   * type 204 No Content and the data field is empty.
   */
  public static void assertResponseIsSuccess(ResultActions testResult) throws Exception {
    String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    testResult.andExpectAll(
        status().is2xxSuccessful(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("$.success", is(true)),
        jsonPath("$.timestamp", lessThan(now)),
        jsonPath("$.data", notNullValue())
    );
  }
  // endregion SUCCESS RESPONSES

  // region ERROR RESPONSES

  /**
   * Asserts that the given response body {@code testResult} content matches with a
   * {@link PermissionException} content and has a data field translated to the given
   * {@code locale}.
   */
  public void assertResponseForPermissionException(ResultActions testResult, Locale locale)
      throws Exception {
    String expectedErrorMessage = appTranslator.generateMessage(PERMISSION_EXCEPTION_KEY, locale);
    String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    testResult.andExpectAll(
        status().isForbidden(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("$.success", is(false)),
        jsonPath("$.timestamp", lessThan(now)),
        jsonPath("$.data", notNullValue()),
        // Data content
        jsonPath("$.data.status", is(HttpStatus.FORBIDDEN.name())),
        jsonPath("$.data.statusCode", is(HttpStatus.FORBIDDEN.value())),
        jsonPath("$.data.message", notNullValue()),
        jsonPath("$.data.message", equalTo(expectedErrorMessage)),
        jsonPath("$.data.debugMesage", nullValue())
    );
  }

  // endregion ERROR RESPONSES
  // endregion UTILS

  // region AUXILIAR METHODS

  // endregion AUXILIAR METHODS
}
