package run.cmid.common.excel.model.entity;

import java.util.List;

import lombok.Getter;
import run.cmid.common.compare.model.LocationTag;

/**
 * 
 * @author leichao
 */
@Getter
public class ExcelResult<T> {

    public ExcelResult(int rownum, LocationTag<T> result, List<CellAddressAndMessage> cellRowErrorList) {
        this.cellRowErrorList = cellRowErrorList;
        this.result = result;
        this.rownum = rownum;
    }

    private List<CellAddressAndMessage> cellRowErrorList;
    private int rownum;
    /**
     * 返回结果
     */
    private LocationTag<T> result;
}
