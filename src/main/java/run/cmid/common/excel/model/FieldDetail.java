package run.cmid.common.excel.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterSimple;
import run.cmid.common.excel.annotations.TableName;
import run.cmid.common.excel.annotations.TableNames;
import run.cmid.common.excel.core.ConverterFieldDetail;
import run.cmid.common.excel.exception.ConverterFieldException;
import run.cmid.common.excel.model.entity.ExcelConverterEntity;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.excel.model.eumns.FieldExceptionType;
import run.cmid.common.excel.model.to.ExcelHeadModel;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * 
 * @author leichao
 * @param <T> 读取原型的数据类型泛型
 */
@Getter
@ToString
public class FieldDetail<T> {
    private FieldDetail(Field field, Class<?> classType, JsonFormat jsonFormat) {
        this.jsonFormat = jsonFormat;
        this.field = field;
        this.fieldName = field.getName();
        this.classType = classType;
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> classType, JsonFormat jsonFormat, ExcelConverter excelConverter,
            TableName name, TableNames names) {
        this(field, classType, jsonFormat);
        this.excelConverter = excelConverter;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;

        if (names != null)
            rangeList = Arrays.asList(names.values());
        else
            rangeList = null;

        if (name == null && names == null)
            throw new NullPointerException("@TableName or @TableNames not Enable");
        if (name != null) {
            this.enumFileName = name.enumGetValueMethodName();
            this.excelRead = new ExcelConverterEntity(excelConverter, name.model(), name.value());
            return;
        }
        if (names != null) {
            this.enumFileName = names.enumGetValueMethodName();
            this.excelRead = new ExcelConverterEntity(excelConverter, names.model(), names.values());
            return;
        }
    }

    public FieldDetail(Field field, Class<?> classType, JsonFormat jsonFormat, ExcelConverterSimple excelConverters,
            int index) {
        this(field, classType, jsonFormat);
        this.excelConverters = excelConverters;
        this.type = FieldDetailType.LIST;
        this.index = index;
        if (!ConverterFieldDetail.IsInterface(field.getType(), List.class)) {
            throw new ConverterFieldException(FieldExceptionType.NOT_SUPPORT_FIELD_TYPE).setMessage(
                    "SUPPORT Interface List,fieldType:" + field.getType().getName() + ",fieldName:" + field.getName());
        }
    }

    public boolean isState(ExcelHeadModel excelHeadModel) {
        if (excelConverter == null && excelHeadModel.isSkipNoAnnotationField())
            return false;// 无注解，跳过开启时返回false
        if (type != FieldDetailType.LIST && !field.getType().isEnum()
                && !ConverterFieldDetail.SupportClassesList.contains(field.getType())) {
            throw new ConverterFieldException(FieldExceptionType.NOT_SUPPORT_FIELD_TYPE)
                    .setMessage("fieldType:" + field.getType().getName() + ",fieldName:" + field.getName());
        }

        return true;
    }

    private ExcelConverterSimple excelConverters;
    private ExcelConverter excelConverter;
    private JsonFormat jsonFormat;
    private Field field;
    private Class<?> classType;
    private List<String> rangeList;
    private int index;
    private FieldDetailType type;
    private String matchValue;
    private final String fieldName;
    private String enumFileName = "";
    private String enumTypeNameFiledValue = "";
    private ExcelConverterEntity excelRead;
    // private boolean state = false;
}
