package run.cmid.common.reader.core;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author leichao
 * @date 2020-04-27 07:42:58
 */
public interface WorkbookInfo {
    void createFile() throws IOException;

    Workbook createWorkbook() throws IOException;
}
