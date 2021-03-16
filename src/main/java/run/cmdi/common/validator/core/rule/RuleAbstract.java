package run.cmdi.common.validator.core.rule;

import lombok.Getter;

@Getter
public abstract class RuleAbstract<TYPE, VALUE> implements RuleSupportValidator<VALUE>, RuleValidator<VALUE> {

    public RuleAbstract(TYPE type, String messages, boolean convertException) {
        this.messages = messages;
        this.type = type;
        this.convertException = convertException;
    }

    public boolean isSupport(Class clazz) {
        return getValueClass().equals(clazz);
    }

    private final TYPE type;
    private final boolean convertException;
    private final String messages;
}
