package run.cmdi.common.reader;

import org.junit.Test;
import run.cmdi.common.reader.model.entity.CellAddressAndMessage;
import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.utils.ReaderUtils;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.Produce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProducePreTest {

    @Test
    public void test() throws IOException {
        //InputStream is = new FileInputStream(new File("C:\\Users\\leichao\\git\\produce-pre\\src\\main\\resources\\data\\测试数据.xlsx"));
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/测试数据.xlsx");
        ReaderUtils p = new ReaderUtils(Produce.class, is);
        EntityResults results = null;
        List<CellAddressAndMessage> errorList = null;
        try {
            results = p.result(0);
            errorList = results.getCellErrorList();
            errorList.forEach((val) -> {
                System.err.println(val.getMessage() + ">>" + val + ">>");
            });
        } catch (ConverterException ex) {
            System.err.println(ex.getErrorMap());
        }
        System.err.println(">>>");
    }
}
