package run.cmid.common.reader.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author leichao
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelConverterHead {
    /**
     * 允许最大发生匹配错误数量 默认3
     */
    int maxWrongCount() default 3;

    /**
     * 开启后跳过没有ExcelConverter的Field字段。默认false
     */
    boolean skipNoAnnotationField() default false;

    /***/
    Index[] indexes() default {};

    /***
     * sheetName 存在时，不进行全sheets搜索，只读取sheetName对应sheet不能存在时抛出异常
     */
    String bookTagName() default "";
}
