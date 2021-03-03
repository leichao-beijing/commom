package run.cmdi.common.validator.model;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class VerificationResult {
    private Map<String, ValidatorFieldException> errorMap;
    private Set<String> headError;
    private Set<String> error;

    public boolean isState() {
        return error.isEmpty() && error.isEmpty();
    }
}
