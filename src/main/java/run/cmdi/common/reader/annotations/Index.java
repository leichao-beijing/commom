package run.cmdi.common.reader.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author leichao
 */
@Target({})
@Retention(RUNTIME)
public @interface Index {
    String[] value() default {};
}
