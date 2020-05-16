package run.cmid.common.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import run.cmid.common.reader.core.ConvertDataToWorkbook;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.core.EntityBuildings;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.DemandTable;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.service.ExcelEntityBuildings;
import run.cmid.common.reader.service.ExcelSaveService;

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
            System.err.println(value+">>"+value.getMessage() );

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

}
