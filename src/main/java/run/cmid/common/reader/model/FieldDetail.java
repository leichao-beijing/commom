package run.cmid.common.reader.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.FindColumn;
import run.cmid.common.reader.annotations.FormatDate;
import run.cmid.common.reader.annotations.Match;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.model.eumns.ConfigError;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.reader.model.eumns.FindModel;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * @author leichao
 */
@Getter
@ToString
public class FieldDetail {
    public FieldDetail(Field field, Class<?> parentClass, FormatDate format, FindColumn findColumn) {
        this.format = format;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.type = FieldDetailType.SINGLE;
        this.model = findColumn.model();
        this.match = findColumn.matches();
        this.checkColumn = findColumn.checkColumn();

        this.max = findColumn.max();
        this.min = findColumn.min();

        this.values = (findColumn.value().length != 0) ? Arrays.asList(findColumn.value())
                : Arrays.asList(field.getName());
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumTypeNameFiledValue = list.get(0).getName();
        }
    }

    public FieldDetail(Field field, Class<?> parentClass, FormatDate format,
                       FindColumn findColumn, int index) {
        this(field, parentClass, format, findColumn);

        if (findColumn.value().length == 0) {
            throw new ConverterExcelConfigException(ConfigError.LIST_ERROR_VALUE_IS_EMPTY);
        }
        this.type = FieldDetailType.LIST;
        this.index = index;
        this.values = Arrays.asList(findColumn.value());
    }

    @Setter
    private boolean checkColumn;
    private final Field field;
    private final Class<?> parentClass;
    FormatDate format;
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
    private List<String> values;
    private final FindModel model;

    private final int max;
    private final int min;

    private final Match[] match;
    @Setter
    private int column = -1;
    /**
     * 是否触发转换异常。
     */
    @Setter
    private boolean converterException = true;
}
