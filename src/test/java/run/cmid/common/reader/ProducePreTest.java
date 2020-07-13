package run.cmid.common.reader;

import org.junit.Test;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.Produce;
import run.cmid.common.reader.model.entity.EntityResults;
import run.cmid.common.reader.utils.ReaderUtils;

import java.io.IOException;
import java.io.InputStream;

public class ProducePreTest {
    @Test
    public void test() throws IOException, ConverterExcelException {
        InputStream is = ReaderUtils.getResourceAsStream("data/NR20040266-城一5G朝阳欢乐谷景区HM-整合后的进度表.xlsx");
        ReaderUtils p = new ReaderUtils(Produce.class, is);
        EntityResults results = p.result();
        results.getCellErrorList().forEach((va) -> {
            System.err.println(va);
        });
    }
}
