package run.cmid.common.excel.core;

import run.cmid.common.excel.model.entity.ExcelResult;

import java.util.List;

public interface ReadBook {
    List<Object> readRow(int rowNum);
    /**
     * 返回-1时，采取读完位置的策略
     * */
    int length();
}