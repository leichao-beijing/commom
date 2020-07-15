package run.cmid.common.validator.exception;

import java.util.List;

public class ValidatorOverlapException extends Exception {
    public ValidatorOverlapException(List<String> overlapList) {
        super(overlapList.toString());
        this.overlapList = overlapList;
    }

    List<String> overlapList;
}
