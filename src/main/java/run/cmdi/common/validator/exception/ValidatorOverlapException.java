package run.cmdi.common.validator.exception;

import lombok.Getter;

import java.util.List;

public class ValidatorOverlapException extends ValidatorErrorException {
    public ValidatorOverlapException(List<String> overlapList) {
        super("Overlap error " + overlapList.toString());
        this.overlapList = overlapList;
    }

    @Getter
    List<String> overlapList;
}
