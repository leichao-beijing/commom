package run.cmid.common.reader.core;

import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.model.entity.ExcelResult;

/**
 * 
 * @author leichao
 */
public interface EntityBuild<T> {

    public EntityResult<T> build() throws ConverterExcelException;

    public ExcelResult<T> build(int rownum);

    public EntityResult<T> buildList(int start, int end);

}
