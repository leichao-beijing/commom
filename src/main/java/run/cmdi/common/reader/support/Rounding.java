package run.cmdi.common.reader.support;

import java.lang.annotation.*;

/**
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rounding {
    int digit();
}
