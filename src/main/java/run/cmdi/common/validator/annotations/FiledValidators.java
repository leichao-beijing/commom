package run.cmdi.common.validator.annotations;

import java.lang.annotation.*;

/**
 * 字段验证器
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledValidators {
    FiledValidator[] value() ;
}
