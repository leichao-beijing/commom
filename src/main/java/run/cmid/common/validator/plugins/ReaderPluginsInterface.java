package run.cmid.common.validator.plugins;

import run.cmid.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;

public interface ReaderPluginsInterface<T extends Annotation> {
    public Class<T> isSupport();

    public void validator(Object value, T t) throws ValidatorException;
}
