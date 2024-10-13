package $package.common.api.error;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class ApiErrorDetails extends Exception {

    private String reason;

    public ApiErrorDetails(String reason) {
        super(reason);
        this.reason = reason;
    }
}
