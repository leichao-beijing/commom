package run.cmdi.common.validator.model;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class VerificationResult<T> {
    private Map<String, ValidatorFieldException> errorMap;
    private Set<String> headError;
    private Set<String> error;
    private T result;

    public VerificationResult<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public boolean isState() {
        return error.isEmpty() && error.isEmpty();
    }
}
