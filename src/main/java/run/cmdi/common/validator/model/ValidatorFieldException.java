package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.validator.annotations.support.FieldName;
import run.cmdi.common.validator.exception.ValidatorException;

public class ValidatorFieldException extends ValidatorException {
    @Getter
    private final String name;
    @Getter
    private final String fieldName;

    public ValidatorFieldException(ValidatorException e, String name, String fieldName) {
        super(e.getType(), e.getMessage());
        this.name = name;
        this.fieldName = fieldName;//FieldNameValue
    }

    public ValidatorFieldException(ValidatorException e, FieldName fieldName) {
        this(e, fieldName.getName(), fieldName.getFieldName());
    }
}
