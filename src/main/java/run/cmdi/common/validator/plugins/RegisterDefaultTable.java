package run.cmdi.common.validator.plugins;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;
import run.cmdi.common.reader.core.MatchValidator;
import run.cmdi.common.validator.annotations.support.*;
import run.cmdi.common.validator.eumns.ValidatorErrorType;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RegisterDefaultTable implements ValidatorPlugin {
    private String fieldName;
    private String name;
    private FieldCompareEntity fieldCompareEntity;
    private FieldNameEntity fieldNameEntity;
    private FieldRequireEntity fieldRequireEntity;
    private FiledValidationEntity filedValidationEntity;
    private FiledValidationsEntity filedValidations;
    //private PastOrPresentEntity pastOrPresentEntity;

    @Override
    public void instanceAnnotationInfo(Field field) {
        this.fieldName = field.getName();
        this.fieldNameEntity = ConverterAnnotation.instance(field, FieldNameEntity.class);
        this.filedValidationEntity = ConverterAnnotation.instance(field, FiledValidationEntity.class);
        this.filedValidations = ConverterAnnotation.instance(field, FiledValidationsEntity.class);

        if (fieldNameEntity != null)
            this.name = this.fieldNameEntity.value();
        else
            this.name = this.fieldName;

        if (filedValidationEntity != null || filedValidations != null)
            state = true;

    }

    private boolean state = false;

    @Override
    public boolean isSupport() {
        return state;
    }

    private List<ValidatorFieldException> validator(ValueFieldName value, Map<String, ValueFieldName> context, FiledValidationEntity fieldValidationEntity) {
        List<ValidatorFieldException> err = new ArrayList<>();
        try {
            List<String> list = MatchValidator.validatorFiledRequire(value, fieldValidationEntity.getFieldRequireEntities(), context);
            if (list != null && list.size() != fieldValidationEntity.getFieldRequireEntities().size()) {//条件全部满足才可执行下部操作
                return err;
            }
            MatchValidator.validatorFiledCompares(value, fieldValidationEntity, context, list);
            MatchValidator.validatorMatch(value, fieldValidationEntity, context, list);
            MatchValidator.validatorSize(value, fieldValidationEntity);
        } catch (ValidatorException e) {
            if (!fieldValidationEntity.isCheck() && e.getType() == ValidatorErrorType.ON_EMPTY)
                return err;//check==false 且  ConverterErrorType.EMPTY 时，忽略empty异常
            err.add(new ValidatorFieldException(e, value));
        }
        return err;
    }

    @Override
    public List<ValidatorFieldException> validator(ValueFieldName value, Map<String, ValueFieldName> context) {
        List<ValidatorFieldException> err = new ArrayList<>();
        if (filedValidations != null) {
            for (FiledValidationEntity fieldValidationEntity : filedValidations.getFieldValidationEntities()) {
                err.addAll(validator(value, context, fieldValidationEntity));
            }
        }
        return err;
    }
}
