package run.cmid.common.reader;

import org.junit.Test;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.exception.ConverterException;
import run.cmid.common.reader.model.Produce;
import run.cmid.common.reader.model.entity.CellAddressAndMessage;
import run.cmid.common.reader.model.entity.EntityResults;
import run.cmid.common.reader.utils.ReaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProducePreTest {
    @Test
    public void test() throws IOException {
        InputStream is = ReaderUtils.getResourceAsStream("data/测试数据.xlsx");
        ReaderUtils p = new ReaderUtils(Produce.class, is);
        try {
            EntityResults results = p.result(0);
            List<CellAddressAndMessage> errorList = results.getCellErrorList();
            errorList.forEach((val) -> {
                System.err.println(val.getMessage() + ">>" + val+">>");
            });
        } catch (ConverterException ex) {
            System.err.println(ex.getErrorMap());
        }


    }
}
