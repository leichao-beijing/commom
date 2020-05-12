package run.cmid.common.excel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 *
 * @author leichao
 * @date 2020-04-29 05:50:09
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableName {
    String value();
    
    /**
     * 当读取对象为枚举对象时，识别该参数。存在时，直接调用参数值的返回值。不存在时，读取枚举对象名称
     */
    String enumGetValueMethodName() default "";
    
    ExcelReadType model() default ExcelReadType.EQUALS;
    
}
