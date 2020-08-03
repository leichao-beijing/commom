package run.cmdi.common.reader.support;

import org.apache.poi.ss.usermodel.Cell;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.validator.exception.ValidatorException;

public class SupportFormatDate implements PluginAnnotation<FormatDate, Cell> {
    @Override
    public Class<FormatDate> getAnnotation() {
        return FormatDate.class;
    }

    @Override
    public String getDescribe() {
        return "FormatDate";
    }

    @Override
    public String getName() {
        return "FormatDate";
    }

    @Override
    public boolean plugin(Object value, Cell cell, FormatDate formatDate) throws ValidatorException {
        return false;
    }
}
