package run.cmid.common.reader.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.ConverterProperty;
import run.cmid.common.reader.annotations.Method;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.model.eumns.ConfigErrorType;
import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * @author leichao
 */
@Getter
@ToString
public class FieldDetail {
    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat, ConverterProperty converterProperty) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.values = (converterProperty.value().length != 0) ? Arrays.asList(converterProperty.value())
                : Arrays.asList(field.getName());
        this.parentClass = parentClass;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;
        this.model = converterProperty.model();
        this.max = converterProperty.max();
        this.methods = converterProperty.methods();
        this.checkColumn = converterProperty.checkColumn();
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat,
                       ConverterProperty converterProperty, int index) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.index = index;
        this.type = FieldDetailType.LIST;
        this.checkColumn = converterProperty.checkColumn();
        this.model = converterProperty.model();
        if (converterProperty.value().length == 0) {
            throw new ConverterExcelConfigException(ConfigErrorType.LIST_ERROR_VALUE_IS_EMPTY);
        }
        this.values = Arrays.asList(converterProperty.value());
        this.max = converterProperty.max();
        this.methods = converterProperty.methods();
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.type = FieldDetailType.SINGLE;
        this.checkColumn = false;
        this.model = ExcelReadType.EQUALS;
        this.values = Arrays.asList(field.getName());
        this.methods = null;
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    @Setter
    private boolean checkColumn;
    private final Field field;
    private final Class<?> parentClass;
    private JsonFormat jsonFormat;
    private int index = -1;
    private FieldDetailType type;
    @Setter
    private String matchValue;

    public String getMatchValue() {
        if (matchValue != null)
            return matchValue;
        return fieldName;
    }

    private final String fieldName;
    private String enumFileName = "";
    private String enumTypeNameFiledValue;
    private final List<String> values;
    private final ExcelReadType model;
    private int max = 100;
    private final Method[] methods;
    @Setter
    private int column = -1;
    /**
     * 是否触发转换异常。
     */
    @Setter
    private boolean converterException = true;
}
