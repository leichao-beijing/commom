package run.cmid.common.excel.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
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
    public FieldDetail(Field field, Class<?> classType, JsonFormat jsonFormat, ExcelConverter excelConverter) {
        this.jsonFormat = jsonFormat;
        this.excelConverter = excelConverter;
        rangeList = Arrays.asList(excelConverter.range());
        this.field = field;
        this.fieldName = field.getName();
        this.classType = classType;
        this.index = 0;
        this.type = FieldDetailType.SINGLE;
        this.excelRead = new ExcelConverterEntity(excelConverter);
        this.enumFileName = excelConverter.enumGetValueMethodName();
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> classType, JsonFormat jsonFormat, ExcelConverter excelConverter,
            int index) {
        this(field, classType, jsonFormat, excelConverter);
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

    private final ExcelConverter excelConverter;
    private final JsonFormat jsonFormat;
    private final Field field;
    private final Class<?> classType;
    private final List<String> rangeList;
    private int index;
    private FieldDetailType type;
    private String matchValue;
    private final String fieldName;
    private String enumFileName = "";
    private String enumTypeNameFiledValue = "";
    private final ExcelConverterEntity excelRead;
    // private boolean state = false;
}
