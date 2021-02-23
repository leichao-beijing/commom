package run.cmdi.common.validator.plugins;

import lombok.Getter;
import run.cmdi.common.validator.annotations.support.FieldName;

@Getter
public class ValueFieldName<T> implements FieldName {

    public static <T> ValueFieldName<T> build(String fieldName, String name, T value) {
        return new ValueFieldName(fieldName, name, value);
    }

    public static ValueFieldName build(String fieldName) {
        return new ValueFieldName(fieldName, fieldName, null);
    }

    private ValueFieldName(String fieldName, String name, T value) {
        this.fieldName = fieldName;
        this.name = name;
        this.value = value;
    }

    private final String fieldName;
    private final String name;
    private final T value;

    @Override
    public String toString() {
        if (name == null)
            return fieldName + " : " + value;
        return name + " : " + value;
    }
}
