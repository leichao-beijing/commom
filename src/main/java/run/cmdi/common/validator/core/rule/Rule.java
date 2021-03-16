package run.cmdi.common.validator.core.rule;

public interface Rule<TYPE, VALUE> extends RuleSupportValidator<VALUE> {
    Rule<TYPE, VALUE> add(RuleModel<TYPE, VALUE> mode, int max);

    Rule<TYPE, VALUE> add(RuleModel<TYPE, VALUE> mode);
}
