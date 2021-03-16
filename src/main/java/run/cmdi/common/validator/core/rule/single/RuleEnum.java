package run.cmdi.common.validator.core.rule.single;

import run.cmdi.common.validator.core.type.Convert;

public class RuleEnum<VALUE extends Enum> extends RuleSingleAbstract<VALUE> implements Convert<VALUE> {
    public RuleEnum(VALUE value, String messages, boolean convertException) {
        super(value, messages, convertException);
    }

    @Override
    public VALUE convert(Object value) {
        return null;
    }

    @Override
    public Class<VALUE> getValueClass() {
        return (Class<VALUE>) getValue().getClass();
    }

}
