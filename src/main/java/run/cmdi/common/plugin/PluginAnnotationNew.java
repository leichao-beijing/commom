package run.cmdi.common.plugin;

import run.cmdi.common.validator.exception.ValidatorException;

public interface PluginAnnotationNew<T extends ConverterAnnotation, CONTEXT> {
    Object plugin(Object value, CONTEXT context, T t) throws ValidatorException;
}
