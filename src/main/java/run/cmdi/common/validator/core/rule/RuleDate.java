package run.cmdi.common.validator.core.rule;

import run.cmdi.common.validator.core.type.Convert;
import run.cmdi.common.validator.core.type.TypeThanEnum;

import java.util.Date;

public class RuleDate extends RuleAbstract<TypeThanEnum, Date> implements Convert<Date> {
    public RuleDate(TypeThanEnum type, String messages, boolean convertException) {
        super(type, messages, convertException);
    }

    @Override
    public Class<Date> getValueClass() {
        return Date.class;
    }


    @Override
    public Date convert(Object value) {
        return null;
    }

    @Override
    public void validator(Date val1, Date val2) {

    }

}
