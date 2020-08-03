package run.cmdi.common.reader.core;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import run.cmdi.common.plugin.PluginAnnotation;

/**
 * @author leichao
 * @date 2020-04-27 07:42:58
 */
public interface WorkbookInfo {

    void close();

    void save(OutputStream out) throws IOException;

    Workbook createWorkbook() throws IOException;

    Map<String, PluginAnnotation> getPlugins();

}
