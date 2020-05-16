package run.cmid.common.reader.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ConfigErrorType;

/**
 * 
 * @author leichao
 */
public class ConverterExcelConfigException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private final ConfigErrorType type;

    @Getter
    private Map<ConfigErrorType, String> errorType;

    public ConverterExcelConfigException(Map<ConfigErrorType, String> errorType) {
        type = null;
        this.errorType = errorType;
    }

    public ConverterExcelConfigException(ConfigErrorType type) {
        this.type = type;
    }

    public ConverterExcelConfigException(ConfigErrorType type, String message) {
        this.type = type;
        this.messageValue = message;
    }

    @Getter
    private String messageValue = "";

    public ConverterExcelConfigException setMessage(String message) {
        this.messageValue = message;
        return this;
    }

    @Override
    public String getMessage() {
        if (errorType != null) {
            String value = "errorList：";
            Iterator<Entry<ConfigErrorType, String>> it = errorType.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ConfigErrorType, String> next = it.next();
                value = value + " [" + next.getValue() + "] ";
            }
            return value;
        }
        if (messageValue == null)
            return type.getTypeName();
        return "错误类型: " + type.getTypeName() + "，错误描述: " + messageValue;
    }
}
