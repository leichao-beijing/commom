package run.cmdi.common.validator.core.rule;

import run.cmdi.common.validator.core.type.Convert;
import run.cmdi.common.validator.core.type.TypeThanEnum;

public class RuleDouble extends RuleAbstract<TypeThanEnum, Double> implements Convert<Double> {


    public RuleDouble(TypeThanEnum typeThanEnum, String messages, boolean convertException) {
        super(typeThanEnum, messages, convertException);
    }

    @Override
    public Double convert(Object value) {
        return null;
    }


    @Override
    public boolean isSupport(Class clazz) {
        return (Double.class.equals(clazz) || double.class.equals(clazz));
    }

    @Override
    public Class<Double> getValueClass() {
        return Double.class;
    }

    @Override
    public void validator(Double val1, Double val2) {

    }
}
