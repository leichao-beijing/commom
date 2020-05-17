package run.cmid.common.reader.service;

import org.apache.poi.ss.usermodel.Workbook;
import run.cmid.common.poi.core.PoiReader;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.core.EntityBuildings;
import run.cmid.common.reader.exception.ConverterExcelException;

public class ExcelEntityBuildings<T> extends EntityBuildings<T> {
    /**
     * @param clazz
     */
    public ExcelEntityBuildings(Class<T> clazz) {
        super(clazz);
    }


    /**
     * 默认从第0行读取头，不进行合并单元格计算。
     *
     * @param workbook
     * @throws ConverterExcelException
     */
    public EntityBuild<T> find(Workbook workbook) throws ConverterExcelException {
        return find(workbook, 0, false);
    }

    /**
     * Excel文件进行匹配，默认不进行合并单元格计算。
     *
     * @param workbook
     * @param readHeadRownum 头读取行
     * @throws ConverterExcelException
     */
    public EntityBuild<T> find(Workbook workbook, int readHeadRownum) throws ConverterExcelException {
        return find(workbook, readHeadRownum, false);
    }

    /**
     * Excel文件进行匹配。
     *
     * @param workbook
     * @param readHeadRownum 头读取行
     * @param rangeState     单元格合并计算
     * @throws ConverterExcelException
     */
    public EntityBuild<T> find(Workbook workbook, int readHeadRownum, boolean rangeState)
            throws ConverterExcelException {
        ReaderPoiConfig readerPoiConfig = new ReaderPoiConfig();
        readerPoiConfig.setCellRangeState(false);
        PoiReader resource = PoiReader.build(workbook, readerPoiConfig, null);
        return find(readHeadRownum, resource);
    }
}
