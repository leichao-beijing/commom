package run.cmid.common.reader.core;

import run.cmid.common.reader.exception.ConverterExcelException;

/**
 * @author leichao
 * @date 2020-04-01 11:00:43
 */
public interface CheckInterface<T, RETURN> {
    RETURN check(T t) throws ConverterExcelException;
}
