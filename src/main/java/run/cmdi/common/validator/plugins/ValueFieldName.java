package run.cmdi.common.validator.plugins;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.register.anntications.RegisterAnnotation;
import run.cmdi.common.validator.annotations.support.FieldName;

import java.lang.reflect.Field;

@Getter
public class ValueFieldName<T> implements FieldName {

    public static <T> ValueFieldName<T> build(String fieldName, String name) {
        return new ValueFieldName(fieldName, name);
    }

    public static <T> ValueFieldName<T> build(String fieldName, String name, T value) {
        ValueFieldName valueFieldName = new ValueFieldName(fieldName, name);
        valueFieldName.setValue(value);
        return valueFieldName;
    }

    public static ValueFieldName build(String fieldName) {
        return new ValueFieldName(fieldName, fieldName);
    }

    private ValueFieldName(String fieldName, String name) {
        this.fieldName = fieldName;
        this.name = name;
    }

    public ValueFieldName build(T value) {
        ValueFieldName valueFieldName = build(this.fieldName, this.name);
        valueFieldName.setValue(value);
        return valueFieldName;
    }

    private String fieldName;
    private String name;

    @Getter
    @Setter
    private T value;

    @Override
    public String toString() {
        if (name == null)
            return fieldName;
        return name;
    }

    @RegisterAnnotation
    public void info(run.cmdi.common.validator.annotations.FieldName fieldName) {
        if (!fieldName.value().equals(""))
            name = fieldName.value();
    }

    @RegisterAnnotation
    public void info(FindColumn column) {
        if (!column.name().equals(""))
            name = column.name();
    }

    @RegisterAnnotation
    public void info(Field filed) {
        this.name = filed.getName();
        this.fieldName = filed.getName();
    }
}
