package run.cmid.common.reader.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.entity.EntityResults;
import run.cmid.common.reader.service.ExcelEntityBuildings;

import java.io.IOException;
import java.io.InputStream;

public class ReaderUtils<T> {
    private Class<T> clazz;
    private InputStream is;

    public ReaderUtils(Class<T> clazz, InputStream is) {
        this.clazz = clazz;
        this.is = is;
    }

    public EntityBuild<T, Sheet, Cell> build() throws IOException, ConverterExcelException {
        return build(0);
    }

    public EntityBuild<T, Sheet, Cell> build(int readRow) throws IOException, ConverterExcelException {
        ExcelEntityBuildings<T> ee = new ExcelEntityBuildings<T>(clazz);
        Workbook workbook = new XSSFWorkbook(is);
        return ee.find(workbook, readRow);
    }

    public EntityResults<T, Sheet, Cell> result(int readRow) throws IOException, ConverterExcelException {
        return build(readRow).build();
    }

    public EntityResults<T, Sheet, Cell> result() throws IOException, ConverterExcelException {
        return result(0);
    }

    public static InputStream getResourceAsStream(String str) {
        return ClassLoader.getSystemResourceAsStream(str);
    }
}
