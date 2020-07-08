package run.cmid.common.validator.annotations;

import java.lang.annotation.*;

/**
 * 字段对应名称
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldName {
    String value();
}
