package run.cmdi.common.reader.exception;

import lombok.Getter;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.List;

public class NotRequireException extends RuntimeException {
    public NotRequireException() {
    }

    public NotRequireException(List<ValidatorFieldException> err) {
        this.err = err;
    }
@Getter
    private List<ValidatorFieldException> err;
}
