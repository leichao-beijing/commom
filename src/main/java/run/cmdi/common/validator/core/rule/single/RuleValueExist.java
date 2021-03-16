package run.cmdi.common.validator.core.rule.single;

import run.cmdi.common.validator.core.type.TypeExistEnum;

public class RuleValueExist extends RuleSingleAbstract<TypeExistEnum> {
    public RuleValueExist(TypeExistEnum typeExistEnum, String messages, boolean convertException) {
        super(typeExistEnum, messages, convertException);
    }

    public static RuleValueExist exist(String messages) {
        return new RuleValueExist(TypeExistEnum.EXIST, messages, false);
    }

    public static RuleValueExist empty(String messages) {
        return new RuleValueExist(TypeExistEnum.EMPTY, messages, false);
    }

    @Override
    public Class<TypeExistEnum> getValueClass() {
        return TypeExistEnum.class;
    }
}
