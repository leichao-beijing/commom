package run.cmid.common.reader.model.entity;

import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ExcelExceptionType;

/**
 * 
 * @author leichao
 */
@Getter
public class CellAddressAndMessage extends CellAddress {
    public CellAddressAndMessage(int row, int column, ExcelExceptionType ex) {
        super(row, column);
        this.ex = ex;
    }

    public CellAddressAndMessage(int row, int column, ExcelExceptionType ex, String message) {
        super(row, column);
        this.ex = ex;
        this.message = message;
    }

    private final ExcelExceptionType ex;
    private String message;
}
