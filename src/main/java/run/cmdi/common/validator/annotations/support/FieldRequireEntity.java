package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.validator.RegexModeInterface;
import run.cmdi.common.validator.annotations.FieldRequire;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.plugin.ConverterAnnotation;
@Getter@Setter
public class FieldRequireEntity extends ConverterAnnotation<FieldRequire> implements FieldRequire , RegexModeInterface {
    @Override
    public void initialize(FieldRequire fieldRequire) {
        this.fieldName = fieldRequire.fieldName();
        this.value = fieldRequire.value();
        this.message = fieldRequire.message();
        this.regex = fieldRequire.regex();
        this.mode = fieldRequire.mode();
    }

    private String fieldName;
    private String[] value;
    private ValidationType mode = ValidationType.EQUALS;
    private String regex = "";
    private String message = "";

    @Override
    public String fieldName() {
        return fieldName;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public ValidationType mode() {
        return mode;
    }

    @Override
    public String regex() {
        return regex;
    }

    @Override
    public String message() {
        return message;
    }
}
