package run.cmid.common.validator;

import run.cmid.common.validator.eumns.ValidationType;

public interface RegexModeInterface {
    String getRegex();

    ValidationType getMode();
}
