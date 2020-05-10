package run.cmid.common.excel.core;

import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.entity.ExcelListResult;
import run.cmid.common.excel.model.entity.ExcelResult;

/**
 * 
 * @author leichao
 */
public interface ExcelBuild<T> {

    public ExcelListResult<T> build() throws ConverterExcelException;

    public ExcelResult<T> build(int rownum);

    public ExcelListResult<T> buildList(int start, int end);

}
