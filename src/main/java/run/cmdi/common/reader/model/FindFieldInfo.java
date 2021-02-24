package run.cmdi.common.reader.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.register.RegisterAnnotationInterface;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.FindColumns;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.register.anntications.RegisterAnnotation;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.reader.model.eumns.FindModel;
import run.cmdi.common.validator.annotations.FieldName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class FindFieldInfo implements RegisterAnnotationInterface {

    private String fieldName;
    private String name;
    private String enumFieldName;

    private FieldDetailType type = FieldDetailType.SINGLE;
    private FindModel findModel;
    private List<String> values = new ArrayList<>();

    private boolean checkColumn;
    private String formatDate;
    private boolean state = false;
    private boolean converterException = true;

    private Integer index;

    private List<FindFieldInfo> list;

    private Integer address;

    @RegisterAnnotation
    public void info(FindColumn findColumn) {
        values = Arrays.asList(findColumn.value());
        this.findModel = findColumn.model();
        this.checkColumn = findColumn.checkColumn();
        if (name == null)
            setName(findColumn.name().equals("") ? fieldName : findColumn.name());
    }

    @RegisterAnnotation
    public void info(FindColumns findColumns) {
        type = FieldDetailType.LIST;
        this.list = new ArrayList<>();
        for (int i = 0; i < findColumns.value().length; i++) {
            FindColumn findColumn = findColumns.value()[i];
            FindFieldInfo info = new FindFieldInfo();
            info.info(findColumn);
            info.setName(findColumn.name().equals("") ? fieldName + " index:" + i : findColumn.name());
            info.setFieldName(getFieldName());
            info.setFormatDate(getFormatDate());
            info.setType(FieldDetailType.LIST);
            info.setIndex(i);
            list.add(info);
        }
    }

    public void info(FieldName fieldName) {
        if (!fieldName.value().equals(""))
            if (name == null || name.equals(""))
                name = fieldName.value();
    }

    @RegisterAnnotation
    public void info(FormatDate formatDate) {
        if (!formatDate.value().equals(""))
            setFormatDate(formatDate.value());
    }

    @RegisterAnnotation
    public void info(Field field) {
        FieldName ann = field.getAnnotation(FieldName.class);
        if (ann != null)
            info(ann);
        if (field.getType().isEnum()) {
            type = FieldDetailType.ENUM;
            for (Field declaredField : field.getType().getDeclaredFields()) {
                JsonValue jsonValue = declaredField.getAnnotation(JsonValue.class);
                if (jsonValue != null && jsonValue.value()) {
                    setEnumFieldName(declaredField.getName());
                    return;
                }
            }
            setEnumFieldName(field.getName());
        }

    }

    public boolean match(Object value) {
          if (value == null)
            return false;
        switch (findModel) {
            case INCLUDE:
                for (String s : values) {
                    if (value.toString().indexOf(s) != -1)
                        return true;
                }
                return false;
            case EQUALS:
                return values.contains(value);
        }
        return false;
    }

    public void exception(Exception e) throws ConverterExcelException {
        if (converterException)
            throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, e.getMessage());
    }

    public void exception(Object value) throws ConverterExcelException {
        if (value == null && converterException)
            throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, name + " 数据:为null" +
                    ((formatDate != null) ? "支持要求" + formatDate : ""));
    }
}
