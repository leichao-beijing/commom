package run.cmdi.common.validator.annotations.support;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;

import java.lang.reflect.Field;

@Getter
@Setter
public class RegisterDefaultTable implements instanceAnnotationInfo {
    private String fieldName;
    private FieldCompareEntity fieldCompareEntity;
    private FieldNameEntity fieldNameEntity;
    private FieldRequireEntity fieldRequireEntity;
    private FiledValidationEntity filedValidationEntity;
    private FiledValidationsEntity filedValidationsEntity;
    private PastOrPresentEntity pastOrPresentEntity;

    @Override
    public void instanceAnnotationInfo(Field field) {
        this.fieldName = field.getName();
        ConverterAnnotation.instance(field, fieldCompareEntity);
        ConverterAnnotation.instance(field, fieldNameEntity);
        ConverterAnnotation.instance(field, fieldRequireEntity);
        ConverterAnnotation.instance(field, filedValidationEntity);
        ConverterAnnotation.instance(field, filedValidationsEntity);
        ConverterAnnotation.instance(field, pastOrPresentEntity);
    }

}
