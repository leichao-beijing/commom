package run.cmid.common.reader.model.eumns;

import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * @author leichao
 */
public enum ExcelConverterException implements EnumName {

    StringIndexOutBounds("字符串长度超出限制。");

    ExcelConverterException(String enumName) {
        this.enumName = enumName;
    }

    @Getter
    private String enumName;

}
