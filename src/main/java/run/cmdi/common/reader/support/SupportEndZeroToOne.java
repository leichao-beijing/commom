package run.cmdi.common.reader.support;

import org.apache.poi.ss.usermodel.Cell;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;

public class SupportEndZeroToOne implements PluginAnnotation<EndZeroToOne, Cell> {
    @Override
    public Class<EndZeroToOne> getAnnotation() {
        return EndZeroToOne.class;
    }

    @Override
    public String getDescribe() {
        return "EndZeroToOne";
    }

    @Override
    public String getName() {
        return "EndZeroToOne";
    }

    @Override
    public boolean plugin(Object value, Cell cell, Annotation t) throws ValidatorException {
        return false;
    }
}
