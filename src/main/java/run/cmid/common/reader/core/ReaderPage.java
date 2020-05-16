package run.cmid.common.reader.core;

import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.HeadInfo;

import java.util.List;

public interface ReaderPage {
    List<Object> readRowList(int rowNum);

    /**
     * 返回-1时，采取读完位置的策略
     */
    int length();

    /**
     * 匹配到book后执行
     */
    void info(HeadInfo headInfo) throws ConverterExcelException;
}