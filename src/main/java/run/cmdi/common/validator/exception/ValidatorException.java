package run.cmdi.common.validator.exception;

import lombok.Getter;
import run.cmdi.common.validator.eumns.ValidatorErrorType;

public class ValidatorException extends RuntimeException {
    @Getter
    private ValidatorErrorType type;

    public ValidatorException(ValidatorErrorType type) {
        super(type.getTypeName());
        this.type = type;
    }

    public ValidatorException(ValidatorErrorType type, String messages) {
        super(messages);
        this.type = type;
    }
}
