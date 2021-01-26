package run.cmdi.common.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Assert;
import org.junit.Test;
import run.cmdi.common.convert.plugs.PoiReaderConvert;
import run.cmdi.common.poi.core.SheetUtils;
import run.cmdi.common.reader.core.ConvertDataToSheetCell;
import run.cmdi.common.reader.core.ConvertDataToWorkbook;
import run.cmdi.common.reader.model.ToExcelModel;
import run.cmdi.common.reader.service.ExcelSaveService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class ConvertDataToWorkbookTest {
    @Test
    public void toWorkbookTest() throws IOException {
        ExcelSaveService excelSaveService = new ExcelSaveService();
        ConvertDataToWorkbook ex = excelSaveService.buildConvert("test", ToExcelModel.class);
        ToExcelModel t = new ToExcelModel("a1", "测试1");
        t.setMessageExcept("除外信息1");
        ex.add(t);
        t = new ToExcelModel("a2", "测试2");
        t.setMessageExcept("除外信息2");
        ex.add(t);

        Set<String> exceptFieldName = new HashSet<>();
        exceptFieldName.add("messageExcept");
        ex = excelSaveService.buildConvert("except", ToExcelModel.class, exceptFieldName);
        t = new ToExcelModel("a1", "测试1");
        t.setMessageExcept("除外信息1");
        ex.add(t);
        t = new ToExcelModel("a2", "测试2");
        t.setMessageExcept("除外信息2");
        ex.add(t);

        Workbook workbook = excelSaveService.getWorkbook();
        Sheet test = workbook.getSheet("test");
        Assert.assertNotNull(test);
        String c2 = SheetUtils.getCell(test, new CellAddress("C2")).orElseThrow(() -> {
            throw new AssertionError("Sheet:test C2 is null ");
        }).toString();
        Assert.assertEquals(c2, "除外信息1");

        Sheet except = workbook.getSheet("except");
        Assert.assertNotNull(except);
        Assert.assertTrue(SheetUtils.getCell(except, new CellAddress("C2")).isEmpty());

        FileOutputStream is = new FileOutputStream(new File("D:\\ceshi.xlsx"));

        workbook.write(is);
        workbook.close();
    }

    @Test
    public void toSheetTest() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream("data/produceTable.xlsx");
        PoiReaderConvert convert = PoiReaderConvert.reader(is);
        Sheet sheet = convert.getWorkbook().getSheet("Sheet1");
        ExcelSaveService excelSaveService = new ExcelSaveService();
        ToExcelModel t = new ToExcelModel("a1", "测试1");
        t.setMessageExcept("除外信息1");
        ConvertDataToSheetCell convertOne = excelSaveService.buildConvert(ToExcelModel.class);
        convertOne.writeSheet(sheet, t);
        is.close();
    }
}
