package run.cmid.common.validator.exception;

import lombok.Getter;
import run.cmid.common.validator.eumns.ValidatorErrorType;

import java.util.List;

public class ValidatorOverlapException extends ValidatorErrorException {
    public ValidatorOverlapException(List<String> overlapList) {
        super("Overlap error " + overlapList.toString());
        this.overlapList = overlapList;
    }

    @Getter
    List<String> overlapList;
}
