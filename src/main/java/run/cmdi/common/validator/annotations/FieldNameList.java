package run.cmdi.common.validator.annotations;

import java.lang.annotation.*;

/**
 * 字段对应名称
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldNameList {
    FieldName[] value();
}
