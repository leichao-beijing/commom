package run.cmid.common.reader.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.FindColumn;
import run.cmid.common.reader.annotations.FormatDate;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.reader.model.eumns.FindModel;
import run.cmid.common.utils.ReflectLcUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leichao
 */
@Getter
@ToString
public class FieldDetail {

    public static FieldDetail build(Field field, Class<?> parentClass, FormatDate format, FindColumn findColumn) {
        return new FieldDetail(field, parentClass, format, findColumn);
    }

    public void converterList(FieldDetail fieldDetail, int index) {
        this.type = FieldDetailType.LIST;
        if (otherDetails == null)
            otherDetails = new ArrayList<>();
        fieldDetail.setIndex(index);
        otherDetails.add(fieldDetail);
    }

    private FieldDetail(Field field, Class<?> parentClass, FormatDate format, FindColumn findColumn) {
        this.format = format;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.type = FieldDetailType.SINGLE;
        this.model = findColumn.model();
        this.checkColumn = findColumn.checkColumn();
        this.values = (findColumn.value().length != 0) ? Arrays.asList(findColumn.value())
                : Arrays.asList(field.getName());
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInFiled(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumFieldName = list.get(0).getName();
        }

    } 

    private List<FieldDetail> otherDetails;
    private final FindModel model;
    private final List<String> values;
    @Setter
    private boolean checkColumn;
    private final Field field;
    private final Class<?> parentClass;
    FormatDate format;
    @Setter
    private int index = 0;
    @Setter
    private FieldDetailType type;
    @Setter
    private String matchValue;

    public String getMatchValue() {
        if (matchValue != null)
            return matchValue;
        return fieldName;
    }

    private final String fieldName;
    private String enumFieldName = "";
    private String name;
    @Setter
    private Integer position = -1;
    /**
     * 是否触发转换异常。
     */
    @Setter
    private boolean converterException = true;
}
