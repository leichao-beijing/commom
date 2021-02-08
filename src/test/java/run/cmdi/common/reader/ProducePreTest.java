package run.cmdi.common.reader;

import org.junit.Test;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuildings;
import run.cmdi.common.reader.core.EntityResultBuildConvert;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.Produce;
import run.cmdi.common.reader.model.entity.EntityResultsConvert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ProducePreTest {

    @Test
    public void test() throws IOException, ConverterException {
        //InputStream is = new FileInputStream(new File("C:\\Users\\leichao\\git\\produce-pre\\src\\main\\resources\\data\\测试数据.xlsx"));
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/测试数据.xlsx");
        ReaderFactory convert = new ReaderFactory(is);

        EntityBuildings<Produce> entityBuildings = new EntityBuildings(Produce.class, new ReaderPoiConfig());
        EntityResultBuildConvert result = entityBuildings.find(convert, 0);
        EntityResultsConvert build = result.build();
        Map list1 = build.getTableErrorMap();
        List<Produce> list = build.getResultList();
        System.err.println(">>>");
    }
}
