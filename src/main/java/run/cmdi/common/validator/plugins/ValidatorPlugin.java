package run.cmdi.common.validator.plugins;

import run.cmdi.common.validator.annotations.support.FieldName;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface ValidatorPlugin extends FieldName {
    void instanceAnnotationInfo(Field field);

    boolean isSupport();

    /**
     * 转换异常抛出状态 true抛出 false忽略
     */
    default boolean isConverterException() {
        return true;
    }


    List<ValidatorFieldException> validator(Map<String, ValueFieldName>  context);
}
