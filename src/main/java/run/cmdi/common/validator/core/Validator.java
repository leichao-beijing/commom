package run.cmdi.common.validator.core;

import run.cmdi.common.validator.plugins.ValidatorPlugin;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.List;
import java.util.Map;

public interface Validator<T> {

    Validator<T> addValidatorsMap(Map<String, List<ValidatorPlugin>> validationStringMap);

    /**
     * 添加扩展注解验证
     */
    Validator<T> addValidator(String fieldName, ValidatorPlugin validatorPlugin);

    List<ValidatorFieldException> validation(T t);

    List<ValidatorFieldException> validationMap(Map<String, Object> context);


    Validator addPlugin(Class<? extends ValidatorPlugin> plugin);


}
