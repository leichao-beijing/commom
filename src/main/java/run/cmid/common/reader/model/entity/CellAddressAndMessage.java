package run.cmid.common.reader.model.entity;

import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

/**
 * @author leichao
 */
@Getter
public class CellAddressAndMessage extends CellAddress {
    public CellAddressAndMessage(int row, int column, ValidatorException ex) {
        super(row, column);
        this.ex = ex.getType();
        this.message = ex.getMessage();
    }

    public CellAddressAndMessage(int row, int column, ConverterErrorType ex, String message) {
        super(row, column);
        this.ex = ex;
        this.message = message;
    }

    public CellAddressAndMessage(int row, int column, ValidatorException ex, String message) {
        super(row, column);
        this.ex = ex.getType();
        if (message != null)
            this.message = message;
        else
            this.message = ex.getMessage();

    }

    private final ConverterErrorType ex;
    private String message;
}
