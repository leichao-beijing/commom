package run.cmid.common.excel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmid.common.excel.model.eumns.ConverterType;
import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 * 
 * 对象内值关联验证Eumn类型对象<br>
 * 验证对象内字段的值是否满足注解所在字段的条件，满足时进行替换或者不进行操作
 * 
 * @author leichao
 * @date 2020-04-01 10:27:38
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelCheckEnumRule {
    /**
     * 注解所在本身方法的对象名称
     */
    String fieldName() default "";

    /**
     * 当check=true时，验证field取到的value是否与value
     * 存在ExcelReadType关系。不存在时抛出异常。
     */
    String[] value();

    /**
     * 当读取对象为枚举对象时，识别该参数。存在时，直接调用参数值的返回值。不存在时，读取枚举对象名称
     */
    String enumGetValueMethodName() default "";

    /**
     * 是否替换，默认不进行替换
     */
    ConverterType type() default ConverterType.NONE;

    ExcelReadType model() default ExcelReadType.EQUALS;
}
