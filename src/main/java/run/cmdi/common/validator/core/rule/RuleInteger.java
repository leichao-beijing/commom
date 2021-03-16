package run.cmdi.common.validator.core.rule;

import run.cmdi.common.validator.core.type.Convert;
import run.cmdi.common.validator.core.type.TypeThanEnum;

public class RuleInteger extends RuleAbstract<TypeThanEnum, Integer> implements Convert<Integer> {


    public RuleInteger(TypeThanEnum typeThanEnum, String messages, boolean convertException) {
        super(typeThanEnum, messages, convertException);
    }

    @Override
    public Integer convert(Object value) {
        return null;
    }

    @Override
    public boolean isSupport(Class clazz) {
        return Integer.class.equals(clazz) || int.class.equals(clazz);
    }

    @Override
    public Class<Integer> getValueClass() {
        return Integer.class;
    }

    @Override
    public void validator(Integer val1, Integer val2) {

    }
}
