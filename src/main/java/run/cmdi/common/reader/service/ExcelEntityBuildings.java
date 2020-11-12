package run.cmdi.common.reader.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import run.cmdi.common.poi.core.PoiReader;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuild;
import run.cmdi.common.reader.core.EntityBuildings;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.exception.ConverterException;

public class ExcelEntityBuildings<T> extends EntityBuildings<T, Sheet, Cell> {


    /**
     * @param clazz
     */
    public ExcelEntityBuildings(Class<T> clazz) {
        super(clazz, new ReaderPoiConfig());
    }

    /**
     * @param clazz
     * @param config
     */
    public ExcelEntityBuildings(Class<T> clazz, ReaderPoiConfig config) {
        super(clazz, config);
    }


    /**
     * 默认从第0行读取头，不进行合并单元格计算。
     *
     * @param workbook
     * @throws ConverterException
     */
    public EntityBuild<T, Sheet, Cell> find(Workbook workbook) throws ConverterException {
        return find(workbook, 0);
    }

    /**
     * Excel文件进行匹配，默认不进行合并单元格计算。
     *
     * @param workbook
     * @param readerHeadNum 头读取行
     * @throws ConverterException
     */
    public EntityBuild<T, Sheet, Cell> find(Workbook workbook, int readerHeadNum) throws ConverterException {
        PoiReader resource = PoiReader.build(workbook, getReaderPoiConfig());
        return find(resource, readerHeadNum);
    }
}
