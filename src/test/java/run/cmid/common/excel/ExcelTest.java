package run.cmid.common.excel;

  import cn.hutool.poi.excel.WorkbookUtil;
  import org.apache.poi.hssf.usermodel.HSSFWorkbook;
  import org.apache.poi.ss.usermodel.Workbook;
  import org.junit.Test;
  import run.cmid.common.excel.core.ExcelBuild;
  import run.cmid.common.excel.core.ExcelBuildings;
  import run.cmid.common.excel.exception.ConverterExcelException;
  import run.cmid.common.excel.model.DemandTable;
  import run.cmid.common.excel.model.entity.ExcelListResult;

  import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

        result.getRusultList().forEach((var)->{
            System.out.println(var.getValue());
        });
    }
}
