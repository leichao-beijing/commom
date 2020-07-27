package run.cmdi.common.validator;

import run.cmdi.common.validator.eumns.ValidationType;

public interface RegexModeInterface {
    String getRegex();

    ValidationType getMode();
}
