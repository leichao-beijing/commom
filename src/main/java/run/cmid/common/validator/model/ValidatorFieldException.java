package run.cmid.common.validator.model;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.validator.exception.ValidatorException;

public class ValidatorFieldException extends ValidatorException {
    @Getter
    private String name;

    public ValidatorFieldException(ValidatorException e, String name) {
        super(e.getType(), e.getMessage());
        this.name = name;
    }
}
