package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

/**
 * 根据如果Method.value 等于@Method所在field的值时， 执行 Method.fieldName从对象中取到值，与Method.compareValue 使用  Method.model进行比较
 *
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
public @interface Method {
    /**
     * 为空时将使用@Method所在的fieldName<br>
     * 当取到的值等于nullOr""时，本配置失效。<br>
     * 对nullOr""敏感，请使用compareValue()={} and check()=true
     */
    String fieldName() default "";

    /**
     * 等于null时，@Method所在的field为任何值时，都会与Method.compareValue进行比较
     */
    String value() default "";

    /**
     * 空值时，不进行mode判断直接通过 。该field进行 model.EQUALS和model.NO_EQUALS。其他匹配方法直接返回false。<br>
     */
    String[] compareValue() default {};

    ExcelReadType model() default ExcelReadType.EQUALS;

    /**
     * 当数据匹配失败时，将会对数据类型进行校验符合类型时。该条false状态，可以转置为true<br>
     */
    ValueType exceptionType() default ValueType.NONE;

    /**
     * 错误提示消息
     */
    String message() default "";

    /**
     * true时，满足fieldName 取值该条件的数据为空时，将会抛出异常<br>
     * 当compareValue() 等于{} 时，该条配置生效。
     */
    boolean check() default false;
}
