package run.cmid.common.validator.plugins;

import run.cmid.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ReaderPluginsInterface<T extends Annotation> {
    public Class<T> getAnnotation();

    /**
     * 具有唯一性的name
     */
    public String getName();

    public void validator(Object value, Map<String, Object> context, T t) throws ValidatorException;
}
