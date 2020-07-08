package run.cmid.common.validator.exception;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

public class ValidatorException extends RuntimeException {
    @Getter
    private ConverterErrorType type;

    public ValidatorException(ConverterErrorType type) {
        super(type.getTypeName());
        this.type = type;
    }

    public ValidatorException(ConverterErrorType type, String messages) {
        super(messages);
        this.type = type;
    }
}
