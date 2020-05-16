package run.cmid.common.reader.exception;

import run.cmid.common.reader.model.eumns.FieldExceptionType;

/**
 * 
 * @author leichao
 */
public class ConverterFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final FieldExceptionType type;

    public ConverterFieldException(FieldExceptionType type) {
        this.type = type;
    }

    private String message;

    public ConverterFieldException setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String getMessage() {
        if (message == null)
            return type.getTypeName();
        return "type: " + type.getTypeName() + ",message: " + message;
    }
}
