package run.cmdi.common.validator.plugins;

import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ReaderPluginsInterface<T extends Annotation> {
     Class<T> getAnnotation();

    /**
     * 具有唯一性的name
     */
     String getName();

     void validator(Object value, Map<String, Object> context, T t) throws ValidatorException;
}
