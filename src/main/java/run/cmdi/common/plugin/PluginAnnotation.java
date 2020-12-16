package run.cmdi.common.plugin;

import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;

public interface PluginAnnotation<T extends Annotation, CONTEXT> {
    Class<T> getAnnotation();

    String getDescribe();

    /**
     * 具有唯一性的name
     */
    String getName();

    /**
     * @return false 执行默认转换器
     */
    boolean plugin(Object value, CONTEXT context, Annotation t) throws ValidatorException;

}
