package run.cmid.common.validator.annotations;

import run.cmid.common.validator.eumns.ValidationType;

import java.lang.annotation.*;

/**
 * 字段验证器
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledValidator {
    /**
     * FiledRequire.FieldName 满足条件时执行后续判断
     */
    FiledRequire[] require() default {};

    /**
     * 判断注解所在值与其他值的关系，不符合抛出异常
     */
    FiledCompare[] fieldValidation() default {};

    String[] value() default {};

    ValidationType mode() default ValidationType.EQUALS;//regex

    /**
     * filedName 符合正则时生效。不存在时忽略
     */
    String regex() default "";

    String message() default "";

    boolean throwState() default false;

    /**
     * 默认不允许出现 null 值。
     * false时，该值为空时将跳过判断条件直接基于通过
     */
    boolean check() default false;

    /**
     * 是否忽略转换异常，默认throw转换异常
     */
    boolean converterException() default true;
}
