package ${package}.common.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import ${package}.common.api.ApiResponseBody;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ErrorApiResponseBody extends ApiResponseBody {

  private final int statusCode;

  private final String status;

  private final String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String debugMessage;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<? extends ApiErrorDetails> errors;
}
