package run.cmdi.common.validator.core.rule;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class RuleModel<TYPE, VALUE> {
    public RuleModel(TYPE type, VALUE value, boolean convertException) {
        this.type = type;
        this.value = value;
        this.convertException = convertException;
    }

    /**
     * 默认响应转换异常
     */
    public RuleModel(TYPE type, VALUE value) {
        this(type, value, true);
    }

    private final TYPE type;
    private final VALUE value;
    private boolean convertException;
    @Setter
    private String messages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleModel<?, ?> ruleModel = (RuleModel<?, ?>) o;
        return Objects.equals(type, ruleModel.type) && Objects.equals(value, ruleModel.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
