package run.cmdi.common.reader;

import org.junit.Test;
import run.cmdi.common.convert.InputStreamConvert;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.EntityBuildings;
import run.cmdi.common.reader.core.EntityResultBuildConvert;
import run.cmdi.common.reader.model.ConvertTestModel;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class InputStreamConvertTest {
    @Test
    public void test1() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/data_utf8.csv");
        InputStreamConvert convert = new InputStreamConvert(is);
        EntityBuildings entityBuildings = new EntityBuildings(ConvertTestModel.class, new ReaderPoiConfig());
        EntityResultBuildConvert en = entityBuildings.find(convert, 0);
        List list = en.build().getResultList();

        System.err.println(">>>");
    }
}
