package run.cmdi.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import run.cmdi.common.poi.core.PoiReader;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuild;
import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.service.ExcelEntityBuildings;

import java.io.IOException;
import java.io.InputStream;

public class ReaderUtils<T> {
    private Class<T> clazz;
    private InputStream is;
    private ReaderPoiConfig config;

    public ReaderUtils(Class<T> clazz, InputStream is) {
        this.clazz = clazz;
        this.is = is;
        this.config = new ReaderPoiConfig();
    }

    public ReaderUtils(Class<T> clazz, InputStream is, ReaderPoiConfig config) {
        this.clazz = clazz;
        this.is = is;
        this.config = config;
    }

    public EntityBuild<T, Sheet, Cell> build() throws IOException, ConverterException {
        return build(0);
    }

    public EntityBuild<T, Sheet, Cell> build(int readRow) throws IOException, ConverterException {
        ExcelEntityBuildings<T> ee = new ExcelEntityBuildings<T>(clazz, config);
        return ee.find(PoiReader.build(is, config), readRow);
    }

    public EntityResults<T, Sheet, Cell> result(int readRow) throws IOException, ConverterException {
        return build(readRow).build();
    }

    public EntityResults<T, Sheet, Cell> result() throws IOException, ConverterException {
        return result(0);
    }

    public static InputStream getResourceAsStream(String str) {
        return ClassLoader.getSystemResourceAsStream(str);
    }
}
