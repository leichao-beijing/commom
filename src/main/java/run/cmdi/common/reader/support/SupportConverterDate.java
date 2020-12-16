package run.cmdi.common.reader.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.validator.exception.ValidatorException;

import java.lang.annotation.Annotation;
import java.util.Date;

public class SupportConverterDate implements PluginAnnotation<JsonFormat, Cell> {

    @Override
    public Class<JsonFormat> getAnnotation() {
        return JsonFormat.class;
    }

    @Override
    public String getDescribe() {
        return "JsonFormat";
    }

    @Override
    public String getName() {
        return "JsonFormat";
    }

    @Override
    public boolean plugin( Object value, Cell cell, Annotation jsonFormat) throws ValidatorException {
        return false;
    }
}
