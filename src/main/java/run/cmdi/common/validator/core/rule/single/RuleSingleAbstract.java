package run.cmdi.common.validator.core.rule.single;

import lombok.Getter;
import run.cmdi.common.validator.core.rule.RuleSupportValidator;

@Getter
public abstract class RuleSingleAbstract<VALUE> implements RuleSupportValidator<VALUE>, RuleSingleValidator<VALUE> {
    public RuleSingleAbstract(VALUE value, String messages, boolean convertException) {
        this.messages = messages;
        this.convertException = convertException;
        this.value = value;
    }

    private final boolean convertException;
    private final String messages;
    private final VALUE value;

    public boolean isSupport(Class clazz) {
        return getValueClass().equals(clazz);
    }

    @Override
    public void validator(VALUE value) {
        if (!this.value.equals(value))
            throw new NullPointerException(getMessages());
    }
}
