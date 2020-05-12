package run.cmid.common.excel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对List对象的转换，不做头验证，不做值校验，只进行数据存储，无数据时存入空字符串。起始index：1
 * 搜索头数据进行匹配，匹配起始与结束为止并将全部存入List数据中
 * 
 * @author leichao
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelConverterList {
    ExcelConverterSimple[] value();
}
