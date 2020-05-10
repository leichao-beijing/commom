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
 * @date 2020-05-01 02:40:12
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelConverterValidator {
    ExcelReadType model() default ExcelReadType.EQUALS;

    String fieldName();

    String validationValue();
}
