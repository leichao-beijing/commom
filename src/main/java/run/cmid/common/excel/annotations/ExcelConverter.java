package run.cmid.common.excel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 * 
 * @author leichao
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelConverter {
    /**
     * 只验证string类型数据<br>
     * check=true时，range不匹配时，抛出异常。<br>
     * range() ={}或false时，不匹配时，不做赋值操作。
     **/
    String[] range() default {};

    ExcelReadType rangeMode() default ExcelReadType.EQUALS;

    boolean check() default false;

    /**
     * 当读取内容为字符串时，最大字符串长度限制
     */
    int max() default 100;

    /**
     * 只有满足fileds内的条件后，该条ExcelConverter 配置的后续才会生效。否则不生效。null时，直接生效配置
     */
    FieldGet[] fileds() default {};
}
