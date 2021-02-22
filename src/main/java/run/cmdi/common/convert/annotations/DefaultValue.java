package run.cmdi.common.convert.annotations;

import java.lang.annotation.*;

/**
 * 产生转换时,当转换至为null时,作为默认值.不存在时,默认值为null
 *
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultValue {
    String value() default "";
}
