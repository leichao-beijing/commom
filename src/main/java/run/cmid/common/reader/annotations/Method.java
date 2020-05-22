package run.cmid.common.reader.annotations;

import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * 当filedName 取得值使用fieldNameModel()与computeValue匹配满足时，<br>
 * 对Master.value 使用mode()匹配满足时Method.value()满足时，结束流程。<br>
 * 规则1：当filename与master.filedName一样时，必须先满足filedName与master 的匹配规则。
 * 规则2：当filedName 取得值等于 "" or NULL 时，不进行fieldNameModel()比较判断通过，进行Mater比较判断。NULL拦截请使用Master.check 进行校验
 * 规则3：Master.value 等于 "" or NULL 时， check()==true时，抛出异常。反之不对 "" or NULL 进行比较判断。
 * 规则4：compareValue and  value == {}  时，不进行逻辑判断直接基于通过。
 *
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Method {

    /**
     * 默认值：Master.filedName
     */
    String fieldName() default "";

    String[] compareValue() default {};

    ExcelReadType fieldNameModel() default ExcelReadType.EQUALS;

    String[] value() default {};

    ExcelReadType model() default ExcelReadType.EQUALS;

    boolean check() default false;

    ValueType exceptionType() default ValueType.NONE;

    String message() default "";

    /**
     * 是否忽略转换异常，默认throw转换异常
     */
    boolean converterException() default true;

    class Validator {
        public static String headMessage(String tagName, Object value) {
            return "<" + tagName + ">的值[" + value + "]";
        }

        public static String message(ExcelReadType mode, String[] values, boolean state) {
            String no = "";
            if (!state)
                no = "不";
            return "在" + Arrays.asList(values) + "内，" + no + "满足 " + mode.getTypeName() + " 条件";
        }

        /**
         * @param value
         * @param values
         * @param mode
         * @return value== NULL or "" 时不进行判断返回null  , values.length==0返回true
         */
        public static boolean mode(Object value, String[] values, ExcelReadType mode) {
            if (StringUtils.isEmpty(value))
                return false;
            if (values.length == 0)
                return true;
            List<String> list = Arrays.asList(values);
            switch (mode) {
                case EQUALS:
                    return list.contains(value);
                case INCLUDE:
                    for (String val : list) {
                        if (value.toString().indexOf(val) != -1)
                            return true;
                    }
                    return false;
                case NO_EQUALS:
                    return !list.contains(value);
                case NO_INCLUDE:
                    for (String val : list) {
                        if (value.toString().indexOf(val) != -1)
                            return false;
                    }
                    return true;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + mode);
            }
        }
    }
}
