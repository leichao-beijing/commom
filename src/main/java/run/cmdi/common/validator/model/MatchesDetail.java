package run.cmdi.common.validator.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.reader.model.eumns.FindModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author leichao
 */
@Getter
@ToString
public class MatchesDetail {
    public MatchesDetail(Field field, Class<?> parentClass, FormatDate format, FindColumn findColumn) {
        this.format = format;
        this.field = field;
        this.fieldName = field.getName();
        this.parentClass = parentClass;
        this.model = findColumn.model();
        this.checkColumn = findColumn.checkColumn();
        this.values = (findColumn.value().length != 0) ? Arrays.asList(findColumn.value())
                : Arrays.asList(field.getName());
        if (field.getType().isEnum()) {
            List<Field> list = ReflectLcUtils.getAnnotationInField(field.getType(), JsonValue.class);
            if (list.size() != 0)
                enumFileName = list.get(0).getName();
        }
    }

    @Setter
    private boolean checkColumn;

    private final Field field;

    private final Class<?> parentClass;

    FormatDate format;

    @Setter
    private String matchValue;

    private final String fieldName;

    private String enumFileName = "";

    private List<String> values;

    private final FindModel model;
}
