package run.cmdi.common.convert.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.convert.RegisterAnnotationInterface;
import run.cmdi.common.convert.annotations.DefaultValue;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.FindColumns;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.reader.annotations.RegisterAnnotation;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.validator.annotations.FieldName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WriterFieldInfo implements RegisterAnnotationInterface {
    private String fieldName;
    private String name;
    private String formatDate;
    private Class clazz;
    private FieldDetailType type = FieldDetailType.SINGLE;
    private List<WriterFieldInfo> list;


    @RegisterAnnotation
    public void info(FieldName fieldName) {
        this.name = fieldName.value();
    }

    @RegisterAnnotation
    public void info(FindColumn findColumn) {
        if (!findColumn.name().equals(""))
            this.name = findColumn.name();
    }

    private String defaultValue;
    @RegisterAnnotation
    public void info(DefaultValue defaultValue) {
        this.defaultValue = defaultValue.value();
        if (list != null) list.forEach(val -> val.setDefaultValue(defaultValue.value()));
    }

    @RegisterAnnotation
    public void info(FindColumns findColumns) {
        type = FieldDetailType.LIST;
        this.list = new ArrayList<>();
        for (int i = 0; i < findColumns.value().length; i++) {
            FindColumn findColumn = findColumns.value()[i];
            WriterFieldInfo info = new WriterFieldInfo();
            info.info(findColumn);
            info.setName(findColumn.name().equals("") ? fieldName + " index:" + i : findColumn.name());
            info.setFieldName(getFieldName());
            info.setFormatDate(getFormatDate());
            info.setDefaultValue(getDefaultValue());
            list.add(info);
        }
    }

    @RegisterAnnotation
    public void info(FormatDate formatDate) {
        this.formatDate = formatDate.value();
    }

    @RegisterAnnotation
    public void info(Field field) {
        this.name = field.getName();
        this.clazz = field.getType();
    }
}
