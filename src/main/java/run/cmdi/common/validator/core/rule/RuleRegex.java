package run.cmdi.common.validator.core.rule;

public class RuleRegex extends RuleAbstract<String, String> {


    public RuleRegex(String s, String messages, boolean convertException) {
        super(s, messages, convertException);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }


    @Override
    public void validator(String val1, String val2) {

    }
}
