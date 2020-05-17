package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

/**
 * 根据如果Method.value 等于@Method所在field的值时， 执行 Method.fieldName从对象中取到值，与Method.compareValue 使用  Method.model进行比较
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
public @interface Method {
    /**
     * 为空时将使用@Method所在的fieldName
     */
    String fieldName() default "";

    /**
     * 等于null时，@Method所在的field为任何值时，都会与Method.compareValue进行比较
     */
    String value() default "";

    /**
     * 不允许空
     */
    String[] compareValue();

    ExcelReadType model() default ExcelReadType.EQUALS;

    /**
     * 当数据匹配失败时，将会对数据类型进行校验符合类型时。该条false状态，可以转置为true<br>
     */
    ValueType exceptionType() default ValueType.NONE;

    /**
     * 错误提示消息
     */
    String message() default "";
}
