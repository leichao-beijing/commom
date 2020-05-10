package run.cmid.common.excel.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;

/**
 * 
 * @author leichao
 */
public class ConverterExcelException extends Exception {
    private static final long serialVersionUID = 1L;
    @Getter
    private final ExcelExceptionType type;

    @Getter
    private Map<ExcelExceptionType, String> errorType;

    public ConverterExcelException(Map<ExcelExceptionType, String> errorType) {
        type = null;
        this.errorType = errorType;
    }

    public ConverterExcelException(ExcelExceptionType type) {
        this.type = type;
    }

    public ConverterExcelException(ExcelExceptionType type, String message) {
        this.type = type;
        this.messageValue = message;
    }

    @Getter
    private String messageValue = "";

    public ConverterExcelException setMessage(String message) {
        this.messageValue = message;
        return this;
    }

    @Override
    public String getMessage() {
        if (errorType != null) {
            String value = "errorList：";
            Iterator<Entry<ExcelExceptionType, String>> it = errorType.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ExcelExceptionType, String> next = it.next();
                value = value + " [" + next.getValue() + "] ";
            }
            return value;
        }
        if (messageValue == null)
            return type.getTypeName();
        return "错误类型: " + type.getTypeName() + "，错误描述: " + messageValue;
    }
}
