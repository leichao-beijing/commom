package run.cmid.common.excel;

  import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import run.cmid.common.excel.core.ExcelBuild;
import run.cmid.common.excel.core.ExcelBuildings;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.DemandTable;
import run.cmid.common.excel.model.entity.ExcelListResult;

/**
 * @author leichao
 */

public class ExcelTest {

    @Test
    public void test() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ExcelBuildings<DemandTable> ee = new ExcelBuildings<DemandTable>(DemandTable.class);
        Workbook  workbook =new HSSFWorkbook(ras);
        ExcelBuild<DemandTable> e = ee.find(workbook);
        ExcelListResult<DemandTable> result = e.build();
        
        result.getErrorType().forEach((ss)->{
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value)->{
            System.err.println(value.getMessage());
        });
        result.getRusultList().forEach((var)->{
            System.out.println(var.getValue());
        });
    }
}
