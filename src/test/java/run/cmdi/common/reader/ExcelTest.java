package run.cmdi.common.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import run.cmdi.common.poi.core.PoiReader;
import run.cmdi.common.poi.core.SheetComment;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.poi.model.StyleInfo;
import run.cmdi.common.reader.core.EntityBuild;
import run.cmdi.common.reader.model.ProduceTable;
import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.validator.core.Validator;
import run.cmdi.common.validator.core.ValidatorTools;
import run.cmdi.common.validator.model.ValidatorFieldException;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.service.ExcelEntityBuildings;
import run.cmdi.common.validator.exception.ValidatorException;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author leichao
 */
@Slf4j
public class ExcelTest {
    @Test
    public void validatorTest() throws IOException, ConverterException {
        EntityResults<ProduceTable, Sheet, Cell> result = getProduceTableTestData();
        Validator<ProduceTable> validator = ValidatorTools.buildValidator(ProduceTable.class);//new ValidatorTools<ProduceTable>(ProduceTable.class);

        result.getCellErrorList().forEach((val) -> {
            System.err.println(val.getMessage());
        });
        result.getResultList().forEach((val) -> {
            List<ValidatorFieldException> error = validator.validation(val.getValue());

            for (ValidatorException ee : error) {
                System.err.println(ee.getType() + ">>>>" + ee.getMessage() + ">>>" + val.getValue().getDemandId());
            }
        });
      log.info("validatorTest done");
    }

    @Test
    public void cloneTest() throws IOException {
        InputStream ras1 = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        InputStream ras2 = getClass().getClassLoader().getResourceAsStream("data/testFunction-2.xlsx");
        PoiReader srcPoi = PoiReader.build(ras1);
        PoiReader desPoi = PoiReader.build(ras2);
        srcPoi.clone(desPoi, "sheet1", "填写注意事项");

        // desPoi.saveAndClose(new File("C:\\Users\\lei_c\\Desktop\\测试\\clone.xlsx"));
        srcPoi.close();
        desPoi.close();
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

        Sheet sheet = poi.getResources().getSheetAt(0);
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
    public void produce() throws IOException, ConverterException {
        EntityResults<ProduceTable, Sheet, Cell> result = getProduceTableTestData();
        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.print(var.getFieldNull() + " ");
            System.err.print(var.getValue().getDesignerDoneDate() + " ");
            System.err.print(var.getValue().getList() + " ");
            System.err.println(var.getValue().getEngineeringSort());
        });
    }


    private EntityResults<ProduceTable, Sheet, Cell> getProduceTableTestData() throws IOException, ConverterException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");
        ExcelEntityBuildings<ProduceTable> ee = new ExcelEntityBuildings<ProduceTable>(ProduceTable.class);
        Workbook workbook = new XSSFWorkbook(ras);
        EntityBuild<ProduceTable, Sheet, Cell> e = ee.find(workbook);
        return e.build();
    }
}
