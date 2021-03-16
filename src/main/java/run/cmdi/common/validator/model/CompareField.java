package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.RegexModeInterface;
import run.cmdi.common.validator.annotations.FieldCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * fieldName or  fieldName 数据比较
 */
@Getter
public class CompareField implements RegexModeInterface {
    private CompareField(FieldCompare fieldCompares, String compareFieldNameSrc, String name) {
        this.fieldName = fieldCompares.fieldName();
        this.message = fieldCompares.message();
        this.mode = fieldCompares.mode();
        this.compareFieldNameSrc = compareFieldNameSrc;
        this.name = name;
        this.regex = fieldCompares.regex();
    }

    private final String compareFieldNameSrc;
    private final String fieldName;
    private final String name;
    private final String message;
    private final String regex;
    private final ValidationType mode;

    /**
     * @param fieldCompares
     * @param compareFieldNameSrc 注解所在的field的 fieldName
     * @param name                注解所在@FieldRule.value()值
     */
    public static List<CompareField> builds(FieldCompare[] fieldCompares, String compareFieldNameSrc, String name) {
        ArrayList<CompareField> list = new ArrayList<CompareField>();
        for (FieldCompare fieldCompare : fieldCompares) {
            list.add(build(fieldCompare, compareFieldNameSrc, name));
        }
        return list;
    }

    /**
     * @param compareFieldNameSrc 注解所在的field的 fieldName
     * @param name                注解所在@FieldRule.value()值
     */
    public static CompareField build(FieldCompare fieldCompare, String compareFieldNameSrc, String name) {
        return new CompareField(fieldCompare, compareFieldNameSrc, name);
    }
}
