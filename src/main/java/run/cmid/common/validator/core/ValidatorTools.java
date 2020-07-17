package run.cmid.common.validator.core;

import lombok.Getter;
import run.cmid.common.validator.EngineObject;
import run.cmid.common.validator.annotations.FieldName;
import run.cmid.common.validator.annotations.FiledValidators;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.EngineClazz;
import run.cmid.common.validator.FunctionClazzInterface;
import run.cmid.common.validator.annotations.FiledValidator;
import run.cmid.common.validator.exception.ValidatorFieldsException;
import run.cmid.common.validator.exception.ValidatorOverlapException;
import run.cmid.common.validator.model.MachModelInfo;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorTools<T> implements FunctionClazzInterface<List<MatchesValidation>> {
    public ValidatorTools(Class<T> t) throws ValidatorOverlapException {
        validationMap = new EngineClazz(t, this).getFieldMap();
        validatorException();
    }

    public ValidatorTools(Map<String, List<MatchesValidation>> validationStringMap) throws ValidatorOverlapException {
        this.validationMap = EngineClazz.getSpotPathMap(validationStringMap);
        validatorException();
    }

    public void validatorException() throws ValidatorOverlapException {
        if (!overlapList.isEmpty())
            throw new ValidatorOverlapException(overlapList);
    }

    private Map<SpotPath, List<MatchesValidation>> validationMap;

    public List<ValidatorFieldException> validation(T t) {
        try {
            new EngineObject(t, validationMap, new ValidatorResultObject()).compute();
        } catch (ValidatorFieldsException e) {
            return e.getErr();
        }
        return new ArrayList<>();
    }

    public List<ValidatorFieldException> validationMap(Map<String, Object> context) {
        ValidatorResultObject v = new ValidatorResultObject();
        MachModelInfo info = new MachModelInfo(context, validationMap);
        return v.compute(null, info);
    }

    @Getter
    private List<String> overlapList;
    private Set<String> setFieldMap;

    @Override
    public List<MatchesValidation> resultField(SpotPath path, Field field) {
        validatorFieldName(field);
        return resultFieldFiledValidator(path, field);
    }

    private void validatorFieldName(Field field) {
        if (overlapList == null)
            overlapList = new ArrayList<>();

        if (setFieldMap == null)
            setFieldMap = new HashSet<>();

        FieldName fieldName = field.getAnnotation(FieldName.class);
        String name;
        if (fieldName != null)
            name = fieldName.value();
        else
            name = field.getName();

        if (setFieldMap.contains(name))
            overlapList.add(name);
        else
            setFieldMap.add(name);
    }

    @Override
    public void validator(Map<SpotPath, List<MatchesValidation>> fieldMap) {

    }

    private List<MatchesValidation> resultFieldFiledValidator(SpotPath path, Field field) {
        List<MatchesValidation> list = new ArrayList<>();
        if (field.getAnnotation(FiledValidator.class) != null)
            list.add(new MatchesValidation(field.getAnnotation(FiledValidator.class), field));

        if (field.getAnnotation(FiledValidators.class) != null)
            for (FiledValidator filedValidator : field.getAnnotation(FiledValidators.class).value()) {
                list.add(new MatchesValidation(filedValidator, field));
            }
        return list;
    }
}
