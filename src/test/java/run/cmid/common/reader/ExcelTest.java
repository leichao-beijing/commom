package run.cmid.common.reader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.DemandTable;
import run.cmid.common.reader.model.ProduceTable;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.service.ExcelEntityBuildings;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author leichao
 */

public class ExcelTest {

    @Test
    public void test() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ExcelEntityBuildings<DemandTable> ee = new ExcelEntityBuildings<DemandTable>(DemandTable.class);
        Workbook workbook = new HSSFWorkbook(ras);
        EntityBuild<DemandTable> e = ee.find(workbook);
        EntityResult<DemandTable> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.println(var);
        });

//
//        ExcelSaveService save=  new ExcelSaveService(new File("D:\\sss.xlsx"));
//        ConvertDataToWorkbook<DemandTable> convert = save.buildConvert("测试", DemandTable.class);
//        result.getResultList().forEach((var) -> {
//            System.out.println(var.getValue());
//            convert.add(var.getValue());
//        });
//        save.save();
    }

    @Test
    public void produce() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");
        ExcelEntityBuildings<ProduceTable> ee = new ExcelEntityBuildings<ProduceTable>(ProduceTable.class);

        Workbook workbook = new XSSFWorkbook(ras);
        EntityBuild<ProduceTable> e = ee.find(workbook);
        EntityResult<ProduceTable> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.println(var);
        });
    }
}
