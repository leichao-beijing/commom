package run.cmid.common.validator.model;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.CompareType;
import run.cmid.common.validator.RegexModeInterface;
import run.cmid.common.validator.annotations.FiledCompare;
import run.cmid.common.validator.eumns.ValidationType;

import java.util.ArrayList;
import java.util.List;

/**
 * filedName or  filedName 数据比较
 */
@Getter
public class CompareField implements RegexModeInterface {
    private CompareField(FiledCompare filedCompares, String compareFieldNameSrc, String name) {
        this.fieldName = filedCompares.fieldName();
        this.message = filedCompares.message();
        this.mode = filedCompares.mode();
        this.compareFieldNameSrc = compareFieldNameSrc;
        this.name = name;
        this.regex = filedCompares.regex();
    }

    private final String compareFieldNameSrc;
    private final String fieldName;
    private final String name;
    private final String message;
    private final String regex;
    private final ValidationType mode;

    /**
     * @param filedCompares
     * @param compareFieldNameSrc 注解所在的field的 fieldName
     * @param name                注解所在@FieldName.value()值
     */
    public static List<CompareField> builds(FiledCompare[] filedCompares, String compareFieldNameSrc, String name) {
        ArrayList<CompareField> list = new ArrayList<CompareField>();
        for (FiledCompare filedCompare : filedCompares) {
            list.add(build(filedCompare, compareFieldNameSrc, name));
        }
        return null;
    }

    /**
     * @param compareFieldNameSrc 注解所在的field的 fieldName
     * @param name                注解所在@FieldName.value()值
     */
    public static CompareField build(FiledCompare filedCompare, String compareFieldNameSrc, String name) {
        return new CompareField(filedCompare, compareFieldNameSrc, name);
    }
}
