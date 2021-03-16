package run.cmdi.common.validator.core.rule.single;

/**
 * 转换失败无视异常
 */
public class RuleBoole extends RuleSingleAbstract<Boolean> {

    public RuleBoole(Boolean bool, String messages, boolean convertException) {
        super(bool, messages, convertException);
    }

    @Override
    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }
}
