package run.cmid.common.excel.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.ToString;
import run.cmid.common.excel.annotations.*;
import run.cmid.common.excel.core.ConverterFieldDetail;
import run.cmid.common.excel.exception.ConverterFieldException;
import run.cmid.common.excel.model.eumns.ExcelReadType;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.excel.model.eumns.FieldExceptionType;
import run.cmid.common.excel.model.to.ExcelHeadModel;
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
        this.values = Arrays.asList(fieldName);
        this.parentClass = parentClass;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;
        this.model = ExcelReadType.EQUALS;

        if (excelConverter != null) {
            this.max = excelConverter.max();
            this.check = excelConverter.check();
            this.fieldGet = excelConverter.fileds();
            this.range=Arrays.asList(excelConverter.range());
        } else {
            this.max = 100;
            this.check = false;
            this.fieldGet = null;
            this.range=null;
        }
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat, ExcelConverter excelConverter, ExcelReadType model, String... values) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;
        this.model = model;
        this.values = Arrays.asList(values);
        if (excelConverter != null) {
            this.max = excelConverter.max();
            this.check = excelConverter.check();
            this.fieldGet = excelConverter.fileds();
            this.range=Arrays.asList(excelConverter.range());
        } else {
            this.max = 100;
            this.check = false;
            this.fieldGet = null;
            this.range=null;
        }
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, JsonFormat jsonFormat, ExcelConverterSimple excelConverterSimple, int index) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.index = index;
        this.type = FieldDetailType.SINGLE;

        this.model = excelConverterSimple.model();
        values = Arrays.asList(excelConverterSimple.value());
        range = Arrays.asList(excelConverterSimple.range());
        this.max = excelConverterSimple.max();
        this.check = excelConverterSimple.check();
        this.fieldGet = excelConverterSimple.fields();
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    private final Field field;
    private final Class<?> parentClass;
    private final JsonFormat jsonFormat;
    private int index;
    private FieldDetailType type;
    private String matchValue;
    private final String fieldName;
    private String enumFileName = "";
    private String enumTypeNameFiledValue = "";
    //private ExcelConverterEntity excelRead;
    private final List<String> values;
    private final List<String> range;
    private final ExcelReadType model;
    private final boolean check;
    private final int max;
    private final FieldGet[] fieldGet;
}
