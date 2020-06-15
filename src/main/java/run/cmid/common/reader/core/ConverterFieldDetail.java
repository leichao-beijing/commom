package run.cmid.common.reader.core;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.hutool.core.util.ReflectUtil;
import run.cmid.common.reader.annotations.FindColumn;
import run.cmid.common.reader.annotations.FindColumns;
import run.cmid.common.reader.annotations.FormatDate;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.to.ExcelHeadModel;
import run.cmid.common.reader.model.to.FindSheetModel;

/**
 * @author leichao
 */
public class ConverterFieldDetail {
    public static <T> List<FieldDetail> toList(Class<T> classes, ExcelHeadModel excelHeadModel,
                                               List<String> indexes) {
        LinkedList<FieldDetail> list = new LinkedList<FieldDetail>();
        Field[] fields = ReflectUtil.getFields(classes);
        FieldDetail fieldDetail = null;
        boolean check = false;
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
                fieldDetail = new FieldDetail(field, classes, format, findColumn);
                if (check)//将 具有唯一性验证的列 添加列空检查
                    fieldDetail.setCheckColumn(true);
                list.add(fieldDetail);
                continue;
            }

            if (findColumns != null) {
                FindColumn[] values = findColumns.value();
                if (values.length == 0) {
                    throw new NullPointerException("ExcelConverterList no data");
                }
                for (int i = 0; i < values.length; i++) {
                    fieldDetail = new FieldDetail(field, classes, format, values[i], i);
                    if (check)
                        fieldDetail.setCheckColumn(true);
                    list.add(fieldDetail);
                }
                continue;
            }
        }
        return list;

    }

    public static <T> FindSheetModel<T> toFindModel(Class<T> classes, ExcelHeadModel excelHeadModel) {
        return new FindSheetModel<T>(toList(classes, excelHeadModel, null));
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
