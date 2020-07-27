package run.cmdi.common.reader.core;

import run.cmdi.common.reader.model.HeadInfo;

import java.util.List;

public interface ReaderPage<PAGE, UNIT> {
    PAGE getPage();

    List<Object> readRowList(int rowNum);

    List<UNIT> readRowUnit(int rowNum);

    /**
     * 返回-1时，采取读完位置的策略
     */
    int length();

    /**
     * 匹配到book后执行
     */
    void info(HeadInfo headInfo) ;
}