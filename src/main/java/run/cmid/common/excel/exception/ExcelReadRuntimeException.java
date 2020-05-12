package run.cmid.common.excel.exception;

import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.eumns.ExcelConverterExceptionType;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class ExcelReadRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("rawtypes")
    @Getter
    private final FieldDetail fieldDetail;
    @Getter
    private final ExcelConverterExceptionType errorType;
    private CellAddress cellAddress;
    private int row;

    public ExcelReadRuntimeException(@SuppressWarnings("rawtypes") FieldDetail fieldDetail,
                                     ExcelConverterExceptionType errorType, CellAddress cellAddress) {
        this.fieldDetail = fieldDetail;
        this.errorType = errorType;
        this.cellAddress = cellAddress;

    }

    public ExcelReadRuntimeException(@SuppressWarnings("rawtypes") FieldDetail fieldDetail,
                                     ExcelConverterExceptionType errorType, int row) {
        this.fieldDetail = fieldDetail;
        this.errorType = errorType;
        this.row = row;
    }

    @Override
    public String getMessage() {
        if (errorType == ExcelConverterExceptionType.StringIndexOutBounds) {
            return "单元格" + cellAddress.toString() + "，出现了" + errorType.getTypeName() + " 错误。最大允许字符串数量 " + fieldDetail.getMax();
        }
        return "第" + row + "行，出现了" + errorType.getTypeName() + " 错误";
    }
}
