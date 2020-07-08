package run.cmid.common.validator.exception;

import lombok.Getter;
import run.cmid.common.validator.model.ValidatorFieldException;

import java.util.List;

public class ValidatorFieldsException extends RuntimeException {
    public ValidatorFieldsException(List<ValidatorFieldException> err) {
        this.err = err;
    }

    @Getter
    private List<ValidatorFieldException> err;
}
