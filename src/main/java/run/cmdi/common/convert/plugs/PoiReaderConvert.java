package run.cmdi.common.convert.plugs;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmdi.common.poi.core.PoiReader;

import java.io.IOException;
import java.io.InputStream;

public class PoiReaderConvert extends PoiReaderAnalysis {

    public static PoiReaderConvert create(boolean xssf) {
        return new PoiReaderConvert(xssf);
    }

    public static PoiReaderConvert reader(InputStream is) {
        return new PoiReaderConvert(is);
    }

    private PoiReaderConvert(InputStream is) {
        if (!super.isSupport(is))
            throw new NullPointerException("no support InputStream");
    }

    /**
     * @param xssf true: xlsx false:xls
     */
    private PoiReaderConvert(boolean xssf) {
        try {
            setWorkbook(WorkbookFactory.create(xssf));
        } catch (IOException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public final boolean isSupport(InputStream is) {
        return true;
    }
}
