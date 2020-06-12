package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.CompareType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledCompare {
    String fieldName();

    String message() default "";

    CompareType mode();


}
