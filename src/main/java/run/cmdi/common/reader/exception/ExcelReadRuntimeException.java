package run.cmdi.common.reader.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.util.CellAddress;
import run.cmdi.common.reader.model.eumns.ExcelConverterException;
import run.cmdi.common.validator.model.MatchesValidation;

/**
 * @author leichao
 */
@Getter
@Setter
public class ExcelReadRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private final MatchesValidation matchesValidation;
    @Getter
    private final ExcelConverterException errorType;
    private CellAddress cellAddress;
    private int row;

    public ExcelReadRuntimeException(@SuppressWarnings("rawtypes") MatchesValidation matchesValidation,
                                     ExcelConverterException errorType, CellAddress cellAddress) {
        this.matchesValidation = matchesValidation;
        this.errorType = errorType;
        this.cellAddress = cellAddress;

    }

    public ExcelReadRuntimeException(MatchesValidation matchesValidation,
                                     ExcelConverterException errorType, int row) {
        this.matchesValidation = matchesValidation;
        this.errorType = errorType;
        this.row = row;
    }

    @Override
    public String getMessage() {
        if (errorType == ExcelConverterException.StringIndexOutBounds) {
            return "单元格" + cellAddress.toString() + "，出现了" + errorType.getTypeName() + " 错误。最大允许字符串数量 " + matchesValidation.getMax();
        }
        return "第" + row + "行，出现了" + errorType.getTypeName() + " 错误";
    }
}
