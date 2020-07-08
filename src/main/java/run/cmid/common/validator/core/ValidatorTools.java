package run.cmid.common.validator.core;

import run.cmid.common.validator.EngineObject;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.EngineClazz;
import run.cmid.common.validator.FunctionClazzInterface;
import run.cmid.common.validator.annotations.FiledValidator;
import run.cmid.common.validator.exception.ValidatorFieldsException;
import run.cmid.common.validator.model.MachModelInfo;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorTools<T> implements FunctionClazzInterface<MatchesValidation> {
    public ValidatorTools(Class<T> t) {
        this.engineClazz = new EngineClazz(t, this);
        validationMap = engineClazz.getFieldMap();
    }

    public ValidatorTools(Map<String, MatchesValidation> validationStringMap) {
        this.validationMap = EngineClazz.getSpotPathMap(validationStringMap);
    }

    private Map<SpotPath, MatchesValidation> validationMap;

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

    private EngineClazz engineClazz;

    @Override
    public MatchesValidation resultField(SpotPath path, Field field) {
        return resultFieldFiledValidator(path, field);
    }

    @Override
    public void validator(Map<SpotPath, MatchesValidation> fieldMap) {

    }

    private MatchesValidation resultFieldFiledValidator(SpotPath path, Field field) {
        if (field.getAnnotation(FiledValidator.class) == null)
            return null;
        FiledValidator filedValidator = field.getAnnotation(FiledValidator.class);
        return new MatchesValidation(filedValidator, field);
    }

}
