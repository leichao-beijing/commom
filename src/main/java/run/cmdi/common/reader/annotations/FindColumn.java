package run.cmdi.common.reader.annotations;

import run.cmdi.common.reader.model.eumns.FindModel;

import java.lang.annotation.*;

/**
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FindColumn {
    String name() default "";

    String[] value() default {};


    FindModel model() default FindModel.EQUALS;

    /**
     * true时，找不到这列时将抛出异常
     */
    boolean checkColumn() default false;

//    /**
//     * true时，将不会抛出转换异常
//     */
//    boolean converterException() default true;
}
