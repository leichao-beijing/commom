package run.cmid.common.validator.eumns;

import org.apache.poi.ss.formula.functions.T;
import run.cmid.common.io.TypeName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * EQUAL LESS_THAN_OR_EQUAL LESS_THAN GREATER_THAN_OR_EQUAL GREATER_THAN <br>
 * 不为数字或无法转换成数字时将抛出非数字无法比较的异常 需要全部满足
 *
 * @author leichao
 */
public enum ValidationType implements TypeName {
    //NONE("不进行操作"),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    NO_EMPTY("非空值"),

    EMPTY("空值"),

    EQUALS("等于"),

    NO_EQUALS("不等于"),

    INCLUDE("包含"),

    NO_INCLUDE("不包含"),

    INTEGER("整数类型"),

    NUMBER("数值类型"),

    LONG("长整形"),

    DOUBLE("双精度"),
    //EXCEPTION("满足条件时,返回条件的消息"),
    //EXECUTE
    EXECUTE("执行"),

    REGEX("正则验证");

    ValidationType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        new File("").exists();
        return typeName;
    }

    public static boolean noValidationValue(ValidationType type) {
        return ALLOW_LIST.contains(type);
    }

    /**
     * 不对values的长度进行校验的验证类型
     */
    public static List<ValidationType> ALLOW_LIST = new ArrayList<ValidationType>() {
        {
            add(ValidationType.INTEGER);
            add(ValidationType.LONG);
            add(ValidationType.DOUBLE);
            add(ValidationType.NO_EMPTY);
            add(ValidationType.EMPTY);
            add(ValidationType.NUMBER);
            add(ValidationType.EXECUTE);
        }
    };
}
