package run.cmid.common.validator.exception;

import lombok.Getter;

import java.util.List;

public class ValidatorFieldsException extends RuntimeException {
    public ValidatorFieldsException(List<ValidatorException> err) {
        this.err = err;
    }

    @Getter
    private List<ValidatorException> err;
}
