package run.cmdi.common.reader.model.entity;

import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import run.cmdi.common.io.TypeName;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.validator.model.ValidatorFieldException;

/**
 * @author leichao
 */
@Getter
public class CellAddressAndMessage extends CellAddress {
    public CellAddressAndMessage(int row, int column, TypeName ex, String message) {
        super(row, column);
        this.ex = ex;
        this.message = message;
    }

    public CellAddressAndMessage(int row, int column, ValidatorFieldException ex) {
        super(row, column);
        this.ex = ex.getType();
        if (ex.getMessage() != null)
            this.message = ex.getMessage();
        else
            this.message = ex.getType().getTypeName();

    }

    public CellAddressAndMessage(int row, int column, ConverterExcelException ex) {
        super(row, column);
        this.ex = ex.getType();
        if (ex.getMessage() != null)
            this.message = ex.getMessage();
        else
            this.message = ex.getType().getTypeName();

    }

    private final TypeName ex;
    private String message;
}
