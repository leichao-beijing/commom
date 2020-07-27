package run.cmdi.common.validator.exception;

import java.util.List;

public class ValidatorNullPointerException extends ValidatorErrorException {
    public ValidatorNullPointerException(List<String> list) {
        super("NullPointer " + list.toString());
        this.list = list;
    }

    private final List<String> list;
}
