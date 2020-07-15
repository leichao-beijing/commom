package run.cmid.common.reader.model.entity;

import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import run.cmid.common.io.TypeName;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.validator.eumns.ValidatorErrorType;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.validator.model.ValidatorFieldException;

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
