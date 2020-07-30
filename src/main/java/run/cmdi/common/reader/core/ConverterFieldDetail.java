package run.cmdi.common.reader.core;

import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.FindColumns;
import run.cmdi.common.reader.model.FieldDetail;
import run.cmdi.common.reader.model.to.ExcelHeadModel;
import run.cmdi.common.reader.model.to.FindSheetModel;
import run.cmdi.common.reader.annotations.FormatDate;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author leichao
 */
public class ConverterFieldDetail {
    public static <T> Map<String, FieldDetail> toMap(Class<T> clazz, ExcelHeadModel excelHeadModel,
                                                     List<String> indexes) {
        HashMap<String, FieldDetail> map = new HashMap<>();
        Field[] fields = ReflectUtil.getFields(clazz);
        FieldDetail fieldDetail;
        boolean check;//TODO 构造fieldDetail
        for (Field field : fields) {
            check = false;
            if (indexes != null && indexes.contains(field.getName()))
                check = true;
            FindColumn findColumn = field.getAnnotation(FindColumn.class);
            FindColumns findColumns = field.getAnnotation(FindColumns.class);
            if (findColumn != null && findColumns != null)
                throw new NullPointerException("@ExcelConverter and @ExcelConverterList Override");
            if (findColumn == null && findColumns == null)
                continue;

            FormatDate format = field.getAnnotation(FormatDate.class);

            if (findColumn != null) {
                fieldDetail = FieldDetail.build(field, clazz, format, findColumn);
                if (check)//将 具有唯一性验证的列 添加列空检查
                    fieldDetail.setCheckColumn(true);
                map.put(field.getName(), fieldDetail);
                continue;
            }

            if (findColumns != null) {
                FindColumn[] values = findColumns.value();
                if (values.length == 0) {//TODO 将配置装配至FieldDetail
                    throw new NullPointerException("@FindColumn no data");
                }
                for (int i = 0; i < values.length; i++) {
                    FieldDetail value = FieldDetail.build(field, clazz, format, values[i]);
                    if (check)
                        value.setCheckColumn(true);
                    fieldDetail = map.get(field.getName());
                    if (fieldDetail == null)
                        map.put(field.getName(), value);
                    else {
                        fieldDetail.converterList(value, i);
                    }
                }
            }
        }
        return map;

    }

    public static <T> FindSheetModel<T> toFindModel(Class<T> classes, ExcelHeadModel excelHeadModel) {
        return new FindSheetModel<T>(toMap(classes, excelHeadModel, null));
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
