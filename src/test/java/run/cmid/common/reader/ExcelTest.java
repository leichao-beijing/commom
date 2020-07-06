package run.cmid.common.reader;

import cn.hutool.crypto.digest.MD5;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.poi.hssf.record.chart.DataFormatRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import run.cmid.common.poi.core.PoiReader;
import run.cmid.common.poi.core.SheetComment;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.poi.model.StyleInfo;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.DemandTable;
import run.cmid.common.reader.model.ProduceTable;
import run.cmid.common.reader.model.entity.EntityResults;
import run.cmid.common.reader.service.ExcelEntityBuildings;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leichao
 */

public class ExcelTest {
    @Test
    public void cloneTest() throws IOException {
        InputStream ras1 = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        InputStream ras2 = getClass().getClassLoader().getResourceAsStream("data/testFunction-2.xlsx");
        PoiReader srcPoi = PoiReader.build(ras1);
        PoiReader desPoi = PoiReader.build(ras2);
        srcPoi.clone(desPoi, "sheet1", "填写注意事项");

        desPoi.saveAndClose(new File("C:\\Users\\lei_c\\Desktop\\测试\\clone.xlsx"));
        srcPoi.close();
        desPoi.close();
    }

    @Test
    public void test123() throws IOException, ConverterExcelException {
        FileInputStream io = new FileInputStream(new File("C:\\java\\common\\src\\test\\resources\\data\\新建测试汇总表.xlsx"));
        PoiReader poi = PoiReader.build(io);
        EntityBuild ee = new ExcelEntityBuildings(DemandTable.class).find(0, poi);
        EntityResults result = ee.build();
        System.err.println(result.getResultList());
    }

    @Test
    public void poiReaderClone() throws IOException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ReaderPoiConfig readerPoiConfig = new ReaderPoiConfig();
        PoiReader poi = PoiReader.build(ras, null, readerPoiConfig, null);
        poi.saveAndClose(new File("D:\\xxx.xls"));

        InputStream ras1 = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");

        ReaderPoiConfig readerPoiConfig1 = new ReaderPoiConfig();
        PoiReader poi1 = PoiReader.build(ras1, null, readerPoiConfig, null);

    }


    @Test
    public void poiReader() throws IOException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ReaderPoiConfig readerPoiConfig = new ReaderPoiConfig();
        PoiReader poi = PoiReader.build(ras, null, readerPoiConfig, null);

        Sheet sheet = poi.getWorkbook().getSheetAt(0);
        SheetComment sd = new SheetComment(sheet);

        StyleInfo info = new StyleInfo();
        Color c = new Color(255, 255, 255);
        info.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        info.setFillForegroundColor(c);
        CellStyle style = poi.createStyle(info);
        Color color = poi.getColor(sheet.getRow(0).getCell(0).getCellStyle());
        Cell cell = sheet.getRow(0).getCell(2);
        cell.setCellStyle(style);
        Comment c1 = sd.getComment("", "c1");
        Comment c2 = sd.getComment("", "c2");
        cell.setCellComment(c1);
        cell.removeCellComment();
        cell.setCellComment(c2);
        cell = sheet.getRow(0).getCell(3);
        cell.setCellStyle(style);
        //cell.setCellComment(com);
        org.apache.poi.ss.usermodel.Color s = cell.getCellStyle().getFillForegroundColorColor();
        poi.saveAndClose(new File("D:\\xxx.xls"));
    }


    @Test
    public void produce() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");
        ExcelEntityBuildings<ProduceTable> ee = new ExcelEntityBuildings<ProduceTable>(ProduceTable.class);

        Workbook workbook = new XSSFWorkbook(ras);
        EntityBuild<ProduceTable, Sheet, Cell> e = ee.find(workbook);
        EntityResults<ProduceTable, Sheet, Cell> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.print(var.getFiledNull() + " ");
            System.err.print(var.getValue().getDesignerDoneDate() + " ");
            System.err.print(var.getValue().getList() + " ");
            System.err.println(var.getValue().getEngineeringSort());
        });
    }
}
