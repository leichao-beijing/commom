package run.cmid.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.eumns.ExcelRead;

@Getter
@Setter
public class MatchesValidation {
    private String fieldName;
    private String name;
    private String format;
    /**
     * 规则前置的条件，满足条件后进行后续规则判断 null时，直接执行后续判断
     */
    private Require require;
    private CompareFiled compareFiled;
    private String[] value;
    private ExcelRead model = ExcelRead.EQUALS;// ExcelRead.NONE;//regex
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
