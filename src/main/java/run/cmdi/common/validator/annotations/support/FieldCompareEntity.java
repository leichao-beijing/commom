package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;
import run.cmdi.common.validator.RegexModeInterface;
import run.cmdi.common.validator.annotations.FieldCompare;
import run.cmdi.common.validator.eumns.ValidationType;
@Getter@Setter
public class FieldCompareEntity extends ConverterAnnotation<FieldCompare> implements FieldCompare, RegexModeInterface {
    @Override
    public void initialize(FieldCompare fieldCompare) {
        this.fieldName = fieldCompare.fieldName();
        this.mode = fieldCompare.mode();
        this.regex = fieldCompare.regex();
        this.message = fieldCompare.message();
    }

    private String fieldName;
    private ValidationType mode = ValidationType.EQUALS;
    private String regex = "";
    private String message = "";

    @Override
    public String fieldName() {
        return fieldName;
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
