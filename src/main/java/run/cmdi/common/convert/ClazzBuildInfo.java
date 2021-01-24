package run.cmdi.common.convert;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.poi.hssf.record.ObjRecord;
import run.cmdi.common.reader.annotations.*;
import run.cmdi.common.reader.core.FieldConfig;
import run.cmdi.common.reader.core.FieldInfo;
import run.cmdi.common.validator.core.Validator;
import run.cmdi.common.validator.core.ValidatorTools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClazzBuildInfo<T> {

    public static <T> ClazzBuildInfo<T> build(Class<T> t) {
        return new ClazzBuildInfo<T>(t);
    }

    private ClazzBuildInfo(Class clazz) {
        this.clazz = clazz;
        this.config = analysisClass(clazz, this);
    }

    @Getter
    private final Class clazz;
    @Getter
    private final FieldConfig config;
    @Getter
    private int maxWrongCount;
    @Getter
    private boolean skipNoAnnotationField;
    @Getter
    private String bookTagName;
    @Getter
    private int startRow = -1;
    @Getter
    private Validator validator;

    private void setHeadInfo(ConverterHead head) {
        this.skipNoAnnotationField = head.skipNoAnnotationField();
        this.maxWrongCount = head.maxWrongCount();
        this.bookTagName = head.bookTagName();
    }

    private static FieldConfig analysisClass(Class<?> clazz, ClazzBuildInfo clazzBuildInfo) {
        ConverterHead head = clazz.getAnnotation(ConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ConverterHead not enable");
        List<FieldInfo> list = new ArrayList<>();
        FieldConfig config = new FieldConfig(list, head.indexes());
        List<String> filedList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            List<FieldInfo> values = toFieldInfo(field);
            if (values != null)
                list.addAll(values);
            filedList.add(field.getName());
        }
        List<String> values = config.getSetIndex().stream().filter((index) -> !filedList.contains(index)).collect(Collectors.toList());
        if (!values.isEmpty())
            throw new NullPointerException("index;" + values.toString() + " Set method no find.");
        clazzBuildInfo.setHeadInfo(head);
        clazzBuildInfo.validator = ValidatorTools.buildValidator(clazz);
        return config;
    }


    private static List<FieldInfo> toFieldInfo(Field field) {
        List<FieldInfo> result = new ArrayList<>();
        FindColumn findColumn = field.getAnnotation(FindColumn.class);
        FindColumns findColumns = field.getAnnotation(FindColumns.class);
        if (findColumn != null && findColumns != null)
            throw new NullPointerException("@ExcelConverter and @ExcelConverterList Override");
        if (findColumn == null && findColumns == null)
            return null;
        FormatDate format = field.getAnnotation(FormatDate.class);
        if (findColumn != null) {
            FieldInfo info = new FieldInfo(findColumn, field.getName());
            if (field.getType().isEnum())
                for (Field declaredField : field.getType().getDeclaredFields()) {
                    JsonValue jsonValue = declaredField.getAnnotation(JsonValue.class);
                    if (jsonValue != null && jsonValue.value()) {
                        info.setEnumFieldName(declaredField.getName());
                        break;
                    }
                }
            if (format != null)
                info.setFormatDate(format.value());
            result.add(info);
        }
        if (findColumns != null) {
            for (int i = 0; i < findColumns.value().length; i++) {
                FieldInfo info = new FieldInfo(findColumns.value()[i], field.getName(), i);
                if (format != null)
                    info.setFormatDate(format.value());
                result.add(info);
            }
        }
        return result;
    }
}
