package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;
import run.cmdi.common.validator.annotations.FieldValidation;
import run.cmdi.common.validator.annotations.FieldValidations;

import java.util.List;

@Getter
@Setter
public class FiledValidationsEntity extends ConverterAnnotation<FieldValidations> implements FieldValidations {
    @Override
    public void initialize(FieldValidations filedValidations) {
        this.value = filedValidations.value();
        this.fieldValidationEntities = build(FiledValidationEntity::new, value);
    }

    private List<FiledValidationEntity> fieldValidationEntities;
    private FieldValidation[] value = new FieldValidation[0];

    @Override
    public FieldValidation[] value() {
        return value;
    }
}
