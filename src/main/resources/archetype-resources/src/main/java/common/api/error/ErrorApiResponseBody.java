package $package.common.api.error;

import $package.common.api.ApiResponseBody;
import $package.common.api.error.ApiErrorDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class ErrorApiResponseBody extends ApiResponseBody {
    private final int statusCode;

    private final String status;

    private final String message;

    private final String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<? extends ApiErrorDetails> errors;
}
