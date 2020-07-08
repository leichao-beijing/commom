package run.cmid.common.reader.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

/**
 * @author leichao
 */
public class ConverterExcelException extends Exception {
    private static final long serialVersionUID = 1L;
    @Getter
    private final ConverterErrorType type;

    @Getter
    private Map<ConverterErrorType, String> errorType;

    public ConverterExcelException(Map<ConverterErrorType, String> errorType) {
        type = null;
        this.errorType = errorType;
    }

    public ConverterExcelException(ConverterErrorType type) {
        this.type = type;
    }

    public ConverterExcelException(ConverterErrorType type, String message) {
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
            String value = "";
            Iterator<Entry<ConverterErrorType, String>> it = errorType.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ConverterErrorType, String> next = it.next();
                value = value + " [" + next.getValue() + "] ";
            }
            return value;
        }
        if (messageValue == null)
            return type.getTypeName();
        return "错误类型: " + type.getTypeName() + "，错误描述: " + messageValue;
    }
}
