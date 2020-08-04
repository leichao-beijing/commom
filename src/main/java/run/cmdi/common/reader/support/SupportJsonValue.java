package run.cmdi.common.reader.support;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.poi.ss.usermodel.Cell;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;

public class SupportJsonValue implements PluginAnnotation<JsonValue, Cell> {
    @Override
    public Class<JsonValue> getAnnotation() {
        return JsonValue.class;
    }

    @Override
    public String getDescribe() {
        return "JsonValue";
    }

    @Override
    public String getName() {
        return "JsonValue";
    }

    @Override
    public Object plugin(Object value, Cell cell, Annotation jsonValue) throws ValidatorException {
        return false;
    }
}
