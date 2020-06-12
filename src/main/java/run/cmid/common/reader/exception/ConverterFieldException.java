package run.cmid.common.reader.exception;

import run.cmid.common.reader.model.eumns.FieldException;

/**
 * 
 * @author leichao
 */
public class ConverterFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final FieldException type;

    public ConverterFieldException(FieldException type) {
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
            return type.getEnumName();
        return "type: " + type.getEnumName() + ",message: " + message;
    }
}
