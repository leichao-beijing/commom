package run.cmid.common.validator.core;

import lombok.Getter;
import org.apache.commons.collections4.Get;
import run.cmid.common.reader.core.MatchValidator;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.EngineClazz;
import run.cmid.common.validator.FunctionClazzInterface;
import run.cmid.common.validator.ResultObjectInterface;
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
        this.map = engineClazz.getStringMap();
    }

    private final Map<String, MatchesValidation> map;

    public List<ValidatorException> validation(T t) {
        try {
            engineClazz.engineObject(t, new ValidatorResultObject()).compute();
        } catch (ValidatorFieldsException e) {
            return e.getErr();
        }
        return new ArrayList<>();
    }

    public List<ValidatorException> validationMap(Map<String, Object> context) {
        ValidatorResultObject v = new ValidatorResultObject();
        MachModelInfo info = new MachModelInfo(context, engineClazz.getFieldMap());
        try {
            v.compute(null, info);
        } catch (ValidatorFieldsException e) {
            return e.getErr();
        }
        return new ArrayList<>();
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
