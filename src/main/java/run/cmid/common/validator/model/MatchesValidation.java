package run.cmid.common.validator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.validator.RegexModeInterface;
import run.cmid.common.validator.eumns.ValidationType;
import run.cmid.common.validator.annotations.FieldName;
import run.cmid.common.validator.annotations.FiledValidator;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
public class MatchesValidation implements RegexModeInterface {
    public MatchesValidation(FiledValidator fieldValidator, Field field) {
        this.name = (field.isAnnotationPresent(FieldName.class)) ? field.getAnnotation(FieldName.class).value() : field.getName();
        this.fieldName = field.getName();
        this.format = (field.isAnnotationPresent(JsonFormat.class)) ? field.getAnnotation(JsonFormat.class).pattern() : null;
        this.value = fieldValidator.value();
        this.mode = fieldValidator.mode();
        this.regex = fieldValidator.regex();
        this.message = fieldValidator.message();
        this.check = fieldValidator.check();
        this.requires = Require.builds(fieldValidator.require(), this.name);
        this.compareFields = CompareField.builds(fieldValidator.fieldValidation(), this.fieldName, this.name);
        this.throwState = fieldValidator.throwState();
    }

    /**
     * 满足前置条件时，不进行判断直接抛出异常。读取messages内值
     */
    private boolean throwState;
    private String fieldName;
    private String name;
    private String format;
    /**
     * 规则前置的条件，满足条件后进行后续规则判断 null时，直接执行后续判断
     */
    private List<Require> requires;
    private List<CompareField> compareFields;
    private String[] value;
    private ValidationType mode = ValidationType.EQUALS;// ExcelRead.NONE;//regex
    /**
     * 默认不允许出现 null 值。
     * false时，该值为空时将跳过判断条件直接基于通过
     */
    private boolean check = false;//default false;
    private String regex = "";
    private String message = "";

    private int max = -1;
    private int min = -1;
}
