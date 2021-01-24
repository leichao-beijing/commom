package run.cmdi.common.reader.core;

import run.cmdi.common.reader.exception.ConverterExcelException;

/**
 * @author leichao
 * @date 2020-04-01 11:00:43
 */
@Deprecated
public interface CheckInterface<T, RETURN> {
    RETURN check(T t) throws ConverterExcelException;
}
