package run.cmid.common.reader.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;

/**
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FindColumn {
    //FindColumn
    String[] value() default {};

    /**
     * 当读取对象为枚举对象时，识别该参数。存在时，直接调用参数值的返回值。不存在时，读取枚举对象名称
     */
    String enumGetValueMethodName() default "";

    FindModel model() default FindModel.EQUALS;

    int max() default -1;

    int min() default -1;

    /**
     * 只有满足fields内的条件后，该条ExcelConverter 配置的后续才会生效。否则不生效。null时，直接生效配置
     */
    Match[] matches() default {};

    /**
     * true时，找不到这列时将抛出异常
     */
    boolean checkColumn() default false;
}
