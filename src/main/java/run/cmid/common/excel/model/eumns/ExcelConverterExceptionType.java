package run.cmid.common.excel.model.eumns;

import lombok.Getter;
import run.cmid.common.io.EnumTypeName;

/**
 * 
 * @author leichao
 */
@Getter
public enum ExcelConverterExceptionType implements EnumTypeName {

    StringIndexOutBounds("字符串长度超出限制。");
    private ExcelConverterExceptionType(String typeName) {
        this.typeName = typeName;
    }

    private String typeName;
}
