package run.cmdi.common.validator.exception;

import lombok.Getter;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.List;

public class ValidatorFieldsException extends RuntimeException {
    public ValidatorFieldsException(List<ValidatorFieldException> err) {
        this.err = err;
    }

    @Getter
    private List<ValidatorFieldException> err;
}
