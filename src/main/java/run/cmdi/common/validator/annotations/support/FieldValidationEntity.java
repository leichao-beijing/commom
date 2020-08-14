package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.validator.RegexModeInterface;
import run.cmdi.common.validator.annotations.FieldRequire;
import run.cmdi.common.validator.annotations.FieldValidation;
import run.cmdi.common.validator.annotations.FieldCompare;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.plugin.ConverterAnnotation;

import java.util.List;

@Getter
@Setter
public class FieldValidationEntity extends ConverterAnnotation<FieldValidation> implements FieldValidation, RegexModeInterface {
    @Override
    public void initialize(FieldValidation fieldValidation) {
        this.require = fieldValidation.require();
        this.fieldValidation = fieldValidation.fieldValidation();
        this.value = fieldValidation.value();
        this.mode = fieldValidation.mode();
        this.regex = fieldValidation.regex();
        this.message = fieldValidation.message();
        this.throwState = fieldValidation.throwState();
        this.check = fieldValidation.check();
        this.converterException = fieldValidation.converterException();
        this.fieldValidation = fieldValidation.fieldValidation();

        this.fieldRequireEntities = build(FieldRequireEntity::new, this.require);
        this.fieldCompareEntities = build(FieldCompareEntity::new, this.fieldValidation);
    }

    private List<FieldRequireEntity> fieldRequireEntities;
    private List<FieldCompareEntity> fieldCompareEntities;
    private FieldRequire[] require = new FieldRequire[0];
    private FieldCompare[] fieldValidation = new FieldCompare[0];
    private String[] value = new String[0];
    private ValidationType mode = ValidationType.EXECUTE;
    private String regex = "";
    private String message = "";
    private boolean throwState = false;
    private boolean check = false;
    private boolean converterException = true;
    private Integer max = -1;
    private Integer min = -1;

    @Override
    public FieldRequire[] require() {
        return require;
    }

    @Override
    public FieldCompare[] fieldValidation() {
        return fieldValidation;
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

    @Override
    public boolean throwState() {
        return throwState;
    }

    @Override
    public boolean check() {
        return check;
    }

    @Override
    public boolean converterException() {
        return converterException;
    }
}
