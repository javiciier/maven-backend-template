package $package.common.exception;

import lombok.*;

@Getter
@NoArgsConstructor
public class InvalidArgumentException extends Exception {
    private String objectName;

    private String field;

    private Object value;

    public InvalidArgumentException(String objectName, String field, Object value) {
        super();
        this.objectName = objectName;
        this.field = field;
        this.value = value;
    }

}
