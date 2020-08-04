package run.cmdi.common.reader.support;

import org.apache.poi.ss.usermodel.Cell;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;

public class SupportRounding implements PluginAnnotation<Rounding, Cell> {
    @Override
    public Class<Rounding> getAnnotation() {
        return Rounding.class;
    }

    @Override
    public String getDescribe() {
        return "Rounding";
    }

    @Override
    public String getName() {
        return "Rounding";
    }

    @Override
    public Object plugin(Object value, Cell cell, Annotation t) throws ValidatorException {
        return null;
    }
}
