package run.cmid.common.excel.annotations;

import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 *
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
public @interface FieldGet {
    String fieldName();

    /**
     * 使用fieldName取toString进行按照 model 类型匹配
     */
    String value();

    ExcelReadType model() default ExcelReadType.EQUALS;
}
