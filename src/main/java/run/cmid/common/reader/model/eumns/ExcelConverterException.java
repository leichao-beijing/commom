package run.cmid.common.reader.model.eumns;

import lombok.Getter;
import run.cmid.common.io.TypeName;

/**
 * @author leichao
 */
public enum ExcelConverterException implements TypeName {

    StringIndexOutBounds("字符串长度超出限制。");

    ExcelConverterException(String typeName) {
        this.typeName = typeName;
    }

    @Getter
    private String typeName;

}
