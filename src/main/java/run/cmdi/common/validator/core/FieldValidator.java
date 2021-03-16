package run.cmdi.common.validator.core;

import lombok.Getter;
import run.cmdi.common.validator.core.rule.RuleContext;
import run.cmdi.common.validator.core.rule.single.RuleSingleAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class FieldValidator extends FieldContext {
    public FieldValidator(String filedName, Class type) {
        super(filedName, type);
        setAlias(filedName);
    }

    public FieldValidator addPre(FieldContext context, RuleSingleAbstract rule) {
        this.pre.add(new RuleContext(context, rule));
        return this;
    }

    public FieldValidator addPre(RuleSingleAbstract rule) {
        this.pre.add(new RuleContext(new FieldContext(this), rule));
        return this;
    }

    public FieldValidator addRule(RuleSingleAbstract rule) {
        this.rule.add(new RuleContext(new FieldContext(this), rule));
        return this;
    }

    private List<RuleContext> rule = new ArrayList<>();
    private List<RuleContext> pre = new ArrayList<>();

    @Override
    public FieldValidator setEmptyField(boolean emptyField) {
        super.setEmptyField(emptyField);
        return this;
    }

    public void validators(Function<FieldContext, Object> fun) {
        Object value = fun.apply(this);
        if (value == null)
            throw new NullPointerException("name:" + getAlias() + " not find object");
        if (pre.size() != 0)
            preValidator(fun);
        if (rule.size() != 0)
            validator(fun);
    }

    private void preValidator(Function<FieldContext, Object> fun) {
        for (RuleContext ruleContext : pre) {
            ruleContext.getRule();
            ruleContext.getRule().validator(fun.apply(ruleContext.getFieldContext()));
        }
    }

    private void validator(Function<FieldContext, Object> fun) {

    }


}
