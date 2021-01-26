package run.cmdi.common.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import run.cmdi.common.convert.plugs.PoiReaderConvert;
import run.cmdi.common.poi.core.PoiReaderClone;
import run.cmdi.common.poi.core.SheetComment;
import run.cmdi.common.poi.model.StyleInfo;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leichao
 */
@Slf4j
public class ExcelTest {

    @Test
    public void cloneTest() throws IOException {
        PoiReaderConvert c1;
        PoiReaderConvert c2;
        InputStream ras1 = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        InputStream ras2 = getClass().getClassLoader().getResourceAsStream("data/testFunction-2.xlsx");
        PoiReaderClone srcPoi = PoiReaderClone.build(c1 = PoiReaderConvert.reader(ras1));
        PoiReaderClone desPoi = PoiReaderClone.build(c2 = PoiReaderConvert.reader(ras2));
        srcPoi.clone(desPoi.getWorkbook(), "sheet1", "填写注意事项");
        //c2.getWorkbook().write(new FileOutputStream(new File("C:\\Users\\lei_c\\Desktop\\测试\\clone.xlsx")));
        c2.close();
        c1.close();
    }


    @Test
    public void poiReader() throws IOException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        PoiReaderConvert convert;
        PoiReaderClone poi = PoiReaderClone.build(convert = PoiReaderConvert.reader(ras));

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
        poi.getWorkbook().write(new FileOutputStream(new File("D:\\xxx.xls")));
        //poi.saveAndClose(new File("D:\\xxx.xls"));
    }
}
