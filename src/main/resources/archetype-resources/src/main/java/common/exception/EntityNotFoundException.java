package ${package}.common.exception;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class EntityNotFoundException extends Exception implements Serializable {

    @NotNull
    private final String entityName;

    @NotNull
    private final Object key;

    public EntityNotFoundException(String entityName, Object key) {
        this.entityName = entityName;
        this.key = key;
    }
}
