package run.cmid.common.validator.model;

import lombok.Getter;
import run.cmid.common.validator.exception.ValidatorException;

public class ValidatorFieldException extends ValidatorException {
    @Getter
    private final String name;
    @Getter
    private final String fieldName;

    public ValidatorFieldException(ValidatorException e, String name, String fieldName) {
        super(e.getType(), e.getMessage());
        this.name = name;
        this.fieldName = fieldName;
    }
}
