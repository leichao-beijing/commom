package run.cmdi.common.reader.annotations;

import java.lang.annotation.*;

/**
 * @author leichao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreReader {
}
