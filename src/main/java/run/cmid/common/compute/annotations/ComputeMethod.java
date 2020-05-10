package run.cmid.common.compute.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据数据建议使用java.lang.Integer java.lang.Double java.lang.Long 对象。
 * 
 * @author leichao
 * @date 2020-04-13 09:08:35
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComputeMethod {
    String value();
}
