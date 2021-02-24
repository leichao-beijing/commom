package run.cmdi.common.validator.core;

import run.cmdi.common.validator.model.ValidatorFieldException;
import run.cmdi.common.validator.plugins.ValidatorPlugin;

import java.util.List;
import java.util.Map;

public interface Validator<T> {

    Validator<T> addValidatorsMap(Map<String, List<ValidatorPlugin>> validationStringMap);

    //boolean isConverter(String fieldName);

    /**
     * 添加扩展注解验证
     */
    Validator<T> addValidator(String fieldName, ValidatorPlugin validatorPlugin);

    Map<String, ValidatorFieldException> validation(T t);

    Map<String, ValidatorFieldException> validationMap(Map<String, Object> context);


    Validator addPlugin(Class<? extends ValidatorPlugin> plugin);


}
