package run.cmdi.common.reader.core;

import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.model.entity.EntityResult;

/**
 * @author leichao
 */
public interface EntityBuild<T, PAGE, UNIT> {

    public EntityResults<T, PAGE, UNIT> build();

    public EntityResult<T, PAGE, UNIT> build(int rownum) throws ConverterExcelException;

    public EntityResults<T, PAGE, UNIT> build(int start, int end) throws ConverterExcelException;

}
