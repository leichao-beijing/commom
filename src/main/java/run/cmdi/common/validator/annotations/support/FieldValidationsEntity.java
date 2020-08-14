package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;
import run.cmdi.common.validator.annotations.FieldValidation;
import run.cmdi.common.validator.annotations.FieldValidations;

import java.util.List;

@Getter
@Setter
public class FieldValidationsEntity extends ConverterAnnotation<FieldValidations> implements FieldValidations {
    @Override
    public void initialize(FieldValidations fieldValidations) {
        this.value = fieldValidations.value();
        this.fieldValidationEntities = build(FieldValidationEntity::new, value);
    }

    private List<FieldValidationEntity> fieldValidationEntities;
    private FieldValidation[] value = new FieldValidation[0];

    @Override
    public FieldValidation[] value() {
        return value;
    }
}
