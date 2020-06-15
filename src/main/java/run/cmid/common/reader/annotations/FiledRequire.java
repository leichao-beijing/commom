package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.ExcelRead;

import java.lang.annotation.*;

/**
 * 生效field的条件
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledRequire {
    /**
     * 默认值：Master.filedName
     */
    String fieldName();

    String[] value();

    ExcelRead model() default ExcelRead.EQUALS;//regex

    /**
     * filedName 符合正则时生效。不存在时忽略
     */
    String regex() default "";

    String message() default "";
}
