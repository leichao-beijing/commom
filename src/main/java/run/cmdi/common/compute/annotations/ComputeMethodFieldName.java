package run.cmdi.common.compute.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定计算方法的读取字段
 *
 * @author leichao
 * @date 2020-04-16 05:17:06
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComputeMethodFieldName {
    String value();
}
