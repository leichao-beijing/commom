package run.cmid.common.excel.core;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.util.ReflectUtil;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterList;
import run.cmid.common.excel.annotations.ExcelConverterSimple;
import run.cmid.common.excel.annotations.TableName;
import run.cmid.common.excel.annotations.TableNames;
import run.cmid.common.excel.exception.ConverterFieldException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.eumns.FieldExceptionType;
import run.cmid.common.excel.model.to.ExcelHeadModel;
import run.cmid.common.excel.model.to.FindSheetModel;

/**
 * 
 * @author leichao
 */
public class ConverterFieldDetail {
    public static <T> List<FieldDetail<T>> toList(Class<T> classes, ExcelHeadModel excelHeadModel) {
        LinkedList<FieldDetail<T>> list = new LinkedList<FieldDetail<T>>();
        Field[] fields = ReflectUtil.getFields(classes);
        FieldDetail<T> fieldDetail = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelConverter.class)) {
                fieldDetail = new FieldDetail<T>(field, classes, field.getAnnotation(JsonFormat.class),
                        field.getAnnotation(ExcelConverter.class), field.getAnnotation(TableName.class),
                        field.getAnnotation(TableNames.class));
                if (fieldDetail.isState(excelHeadModel))
                    list.add(fieldDetail);
                continue;
            }
             if (field.isAnnotationPresent(ExcelConverterList.class)) {
                ExcelConverterList excelConverterStringList = field.getAnnotation(ExcelConverterList.class);
                ExcelConverterSimple[] values = excelConverterStringList.value();
                for (int i = 0; i < values.length; i++) {
                    fieldDetail = new FieldDetail<T>(field, classes, field.getAnnotation(JsonFormat.class), values[i],
                            i);
                    if (fieldDetail.isState(excelHeadModel))
                        list.add(fieldDetail);
                }
                continue;
            }

            fieldDetail = new FieldDetail<T>(field, classes, field.getAnnotation(JsonFormat.class),
                    field.getAnnotation(ExcelConverter.class));
            if (fieldDetail.isState(excelHeadModel)) {
                list.add(fieldDetail);
                continue;
            }
            throw new ConverterFieldException(FieldExceptionType.NOT_SUPPORT_FIELD_TYPE)
                    .setMessage("fieldType:" + field.getType().getName() + ",fieldName:" + field.getName());
        }
        return list;

    }

    /**
     * 功能废弃
     */
    public static <T> FieldDetail<T> addCheckInteface(FieldDetail<T> fieldDetail, Field field) {
//        if (field.isAnnotationPresent(ExcelCheckStringRule.class))
//            fieldDetail.setStringRule(field.getAnnotation(ExcelCheckStringRule.class));
//        if (field.isAnnotationPresent(ExcelCheckEumnRule.class))
//            fieldDetail.setEumnRule(field.getAnnotation(ExcelCheckEumnRule.class));
        return fieldDetail;
    }

    public static <T> FindSheetModel<T> toFindModel(Class<T> classes, ExcelHeadModel excelHeadModel) {
        return new FindSheetModel<T>(toList(classes, excelHeadModel));
    }

    /**
     * 验证classes 中是否存在interfaceClasses接口类
     */
    public static boolean IsInterface(Class<?> classes, Class<?> interfaceClasses) {
        Class<?>[] is = classes.getInterfaces();
        if (classes.equals(interfaceClasses)) {
            return true;
        }
        for (Class<?> i : is) {
            if (i.equals(interfaceClasses))
                return true;
        }
        return false;
    }

    public static final Set<Class<?>> SupportClassesList = new HashSet<Class<?>>() {
        private static final long serialVersionUID = 1L;
        {
            add(String.class);

            add(int.class);
            add(Integer.class);

            add(double.class);
            add(Double.class);

            add(long.class);
            add(Long.class);

            add(float.class);
            add(Float.class);

            add(short.class);
            add(Short.class);

            add(boolean.class);
            add(Boolean.class);

            add(Date.class);
        }

    };

}
