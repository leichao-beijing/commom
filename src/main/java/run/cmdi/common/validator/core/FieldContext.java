package run.cmdi.common.validator.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldContext {
    public static FieldContext build(String filedName, Class type) {
        return new FieldContext(filedName, type);
    }

    public FieldContext(String filedName, Class type) {
        this.filedName = filedName;
        this.type = type;
    }

    public FieldContext(FieldContext context) {
        this.filedName = context.getFiledName();
        this.type = context.getType();
        this.alias = context.getAlias();
        this.emptyField = context.isEmptyField();
    }

    public FieldContext alias(String alias) {
        this.alias = alias;
        return this;
    }

    private final String filedName;
    private final Class type;
    private String alias;
    /**
     * fieldName无法拿到对应值时,是否抛出异常.
     * 默认抛出异常
     */
    private boolean emptyField = true;

    public FieldContext setEmptyField(boolean emptyField) {
        this.emptyField = emptyField;
        return this;
    }
}
