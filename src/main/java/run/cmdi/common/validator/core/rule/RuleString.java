package run.cmdi.common.validator.core.rule;

import run.cmdi.common.validator.core.type.TypeStringEnum;

public class RuleString extends RuleAbstract<TypeStringEnum, String> {

    public RuleString(TypeStringEnum typeStringEnum, String messages, boolean convertException) {
        super(typeStringEnum, messages, convertException);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void validator(String val1, String val2) {
        //todo 实现校验
    }
}
