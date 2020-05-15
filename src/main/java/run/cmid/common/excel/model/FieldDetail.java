package run.cmid.common.excel.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterSimple;
import run.cmid.common.excel.annotations.Method;
import run.cmid.common.excel.exception.ConverterExcelConfigException;
import run.cmid.common.excel.model.eumns.ConfigErrorType;
import run.cmid.common.excel.model.eumns.ExcelReadType;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * @param <T> 读取原型的数据类型泛型
 * @author leichao
 */
@Getter
@ToString
public class FieldDetail<T> {
    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat, ExcelConverter excelConverter) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.values = (excelConverter.values().length != 0) ? Arrays.asList(excelConverter.values())
                : Arrays.asList(field.getName());
        this.parentClass = parentClass;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;
        this.model = excelConverter.model();
        this.max = excelConverter.max();
        this.methods = excelConverter.methods();
        this.nullCheck = excelConverter.checkNull();
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat,
            ExcelConverterSimple excelConverterSimple, int index) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.index = index;
        this.type = FieldDetailType.LIST;
        this.nullCheck = excelConverterSimple.checkNull();
        this.model = excelConverterSimple.model();
        if (excelConverterSimple.value().length == 0) {
            throw new ConverterExcelConfigException(ConfigErrorType.LIST_ERROR_VALUE_IS_EMPTY);
        }
        this.values = Arrays.asList(excelConverterSimple.value());
        this.max = excelConverterSimple.max();
        this.methods = excelConverterSimple.methods();
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
        this.nullCheck = false;
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
    private boolean nullCheck;
    private final Field field;
    private final Class<?> parentClass;
    private JsonFormat jsonFormat;
    private int index = -1;
    private FieldDetailType type;
    private String matchValue;
    private final String fieldName;
    private String enumFileName = "";
    private String enumTypeNameFiledValue = "";
    private final List<String> values;
    private final ExcelReadType model;
    private int max = 100;
    private final Method[] methods;
    @Setter
    private int column = -1;
}
