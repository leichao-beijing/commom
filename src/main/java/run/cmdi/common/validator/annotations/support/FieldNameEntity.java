package run.cmdi.common.validator.annotations.support;

import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.plugin.ConverterAnnotation;

public class FieldNameEntity extends ConverterAnnotation<FieldName> implements FieldName {

    @Override
    public void initialize(FieldName fieldName) {
        this.value = fieldName.value();
    }

    private String value;

    @Override
    public String value() {
        return value;
    }
}
