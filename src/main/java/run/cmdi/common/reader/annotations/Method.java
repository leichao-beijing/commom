package run.cmdi.common.reader.annotations;

import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.eumns.Value;

import java.lang.annotation.*;

/**
 * 当fieldName 取得值使用fieldNameModel()与computeValue匹配满足时，<br>
 * 对Master.value 使用mode()匹配满足时Method.value()满足时，结束流程。<br>
 * 规则1：当fieldName与master.fieldName一样时，必须先满足fieldName与master 的匹配规则。
 * 规则2：当fieldName 取得值等于 "" or NULL 时，不进行fieldNameModel()比较判断通过，进行Mater比较判断。NULL拦截请使用Master.check 进行校验
 * 规则3：Master.value 等于 "" or NULL 时， check()==true时，抛出异常。反之不对 "" or NULL 进行比较判断。
 * 规则4：compareValue and  value == {}  时，不进行逻辑判断直接基于通过。
 *
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Method {

    /**
     * 默认值：Master.fieldName
     */
    String fieldName() default "";

    String[] compareValue() default {};

    ValidationType fieldNameModel() default ValidationType.EQUALS;//regex

    /**
     * fieldName 符合正则时生效。不存在时忽略
     */
    String fieldRegex() default "";

    String regex() default "";


    String[] value() default {};

    ValidationType model() default ValidationType.EQUALS;

    boolean check() default false;

    Value exceptionType() default Value.NONE;

    String message() default "";

    /**
     * 是否忽略转换异常，默认throw转换异常
     */
    boolean converterException() default true;
}
