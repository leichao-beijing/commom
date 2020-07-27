package run.cmdi.common.reader.exception;

import run.cmdi.common.reader.model.eumns.FieldException;

/**
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
            return type.getTypeName();
        return "type: " + type.getTypeName() + ",message: " + message;
    }
}
