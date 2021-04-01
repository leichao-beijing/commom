package run.cmdi.common.reader;

import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuildings;
import run.cmdi.common.reader.core.EntityResultBuildConvert;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.Produce;
import run.cmdi.common.reader.model.Project;
import run.cmdi.common.reader.model.entity.CellAddressAndMessage;
import run.cmdi.common.reader.model.entity.EntityResultsConvert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducePreTest {

    //@Test
    public void test() throws IOException, ConverterException {
        InputStream is = new FileInputStream(new File("C:\\Users\\leichao\\Desktop\\项目表-10个mis201229.xlsx"));
        //InputStream is = getClass().getClassLoader().getResourceAsStream("data/测试数据.xlsx");
        ReaderFactory convert = new ReaderFactory(is);

        EntityBuildings<Produce> entityBuildings = new EntityBuildings(Project.class, new ReaderPoiConfig());
        EntityResultBuildConvert result = entityBuildings.find(convert, 0);
        EntityResultsConvert build = result.build();
        Map<Integer, Map<Integer, CellAddressAndMessage>> list1 = build.getTableErrorMap();
        Map<String, CellAddressAndMessage> map = new HashMap<String, CellAddressAndMessage>();
        list1.forEach((row, column) -> column.forEach((c, value) -> {
            map.put(new CellAddress(row, c).toString(), value);
        }));
        List<Produce> list = build.getResultList();
        //System.err.println(">>>");
    }
}
