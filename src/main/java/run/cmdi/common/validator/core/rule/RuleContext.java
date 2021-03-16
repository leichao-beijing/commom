package run.cmdi.common.validator.core.rule;

import lombok.Getter;
import run.cmdi.common.validator.core.FieldContext;

@Getter
public class RuleContext {
    public RuleContext(FieldContext fieldContext, RuleSupportValidator rule) {
        this.rule = rule;
        this.fieldContext = fieldContext;
    }

    private RuleSupportValidator rule;
    private FieldContext fieldContext;
}
