package run.cmdi.common.reader;

import org.junit.Test;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuildings;
import run.cmdi.common.reader.core.EntityResultBuildConvert;
import run.cmdi.common.reader.model.ConvertTestModel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReaderFactoryTest {
    @Test
    public void test1() throws Exception {
        Map<String,Integer>map=new TreeMap();
        map.put("222",22);
        map.put("2222",22);

        InputStream is = getClass().getClassLoader().getResourceAsStream("data/data_utf8.csv");
        ReaderFactory convert = new ReaderFactory(is);
        EntityBuildings entityBuildings =   new EntityBuildings(ConvertTestModel.class, new ReaderPoiConfig());
        EntityResultBuildConvert en = entityBuildings.find(convert, 0);
        List list = en.build().getResultList();

        System.err.println(">>>");
    }
}
