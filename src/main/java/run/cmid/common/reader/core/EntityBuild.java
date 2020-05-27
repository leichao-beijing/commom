package run.cmid.common.reader.core;

import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.model.entity.EntityResults;

/**
 * @author leichao
 */
public interface EntityBuild<T, PAGE, UNIT> {

    public EntityResults<T, PAGE, UNIT> build() throws ConverterExcelException;

    public EntityResult<T, PAGE, UNIT> build(int rownum);

    public EntityResults<T, PAGE, UNIT> build(int start, int end);

}
