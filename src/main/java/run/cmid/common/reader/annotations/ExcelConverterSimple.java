package run.cmid.common.reader.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

/**
 * 
 * @author leichao
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelConverterSimple {
    /** 不能等于空 */
    String[] value();

    /**
     * 只验证string类型数据<br>
     * check=true时，range不匹配时，抛出异常。<br>
     * range() ={}或false时，不匹配时，不做赋值操作。
     **/
    String[] range() default {};

    ExcelReadType model() default ExcelReadType.EQUALS;

    /**
     * 当读取对象为枚举对象时，识别该参数。存在时，直接调用参数值的返回值。不存在时，读取枚举对象名称
     */
    String enumGetValueMethodName() default "";

    /**
     * true时，不允许出现null数据
     */
    boolean checkNull() default false;

    /**
     * 当读取内容为字符串时，最大字符串长度限制
     */
    int max() default 100;

    /**
     * 只有满足fileds内的条件后，该条ExcelConverter 配置的后续才会生效。否则不生效。null时，直接生效配置
     */
    Method[] methods() default {};

    ExcelReadType rangeMode() default ExcelReadType.EQUALS;

    ValueType classType() default ValueType.NONE;
}
