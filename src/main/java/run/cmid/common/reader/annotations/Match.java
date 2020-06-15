package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.ExcelRead;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Match {
    /**
     * 符合条件进行下层判断。不存在时，直接进行下一层判断
     */
    FiledRequire[] require() default {};

    /**
     * 指定filedName的值与 Matches所在的值进行比较大小不符合时报错
     */
    FiledCompare[] filedCompares() default {};

    String[] value() default {};

    ExcelRead model() default ExcelRead.EXISTS;//regex

    /**
     * filedName 符合正则时生效。不存在时忽略
     */
    String regex() default "";

    String message() default "";

    /**
     * 默认不允许出现 null 值。
     * false时，该值为空时将跳过判断条件直接基于通过
     */
    boolean check() default true;

    /**
     * 是否忽略转换异常，默认throw转换异常
     */
    boolean converterException() default true;
}
