package run.cmdi.common.validator.annotations;

import run.cmdi.common.validator.eumns.ValidationType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldCompare {
    String fieldName();

    ValidationType mode() default ValidationType.EQUALS;//regex

    /**
     * model== ExcelRead.NONE 时匹配正则 符合正则时生效。不存在时忽略 只验证转换对象的toString类型
     */
    String regex() default "";

    String message() default "";

}
