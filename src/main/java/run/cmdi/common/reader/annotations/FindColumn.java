package run.cmdi.common.reader.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import run.cmdi.common.reader.model.eumns.FindModel;

/**
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FindColumn {
    //FindColumn
    String[] value() default {};


    FindModel model() default FindModel.EQUALS;

    /**
     * true时，找不到这列时将抛出异常
     */
    boolean checkColumn() default false;
}
