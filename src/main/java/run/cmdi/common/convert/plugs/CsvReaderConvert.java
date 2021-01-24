package run.cmdi.common.convert.plugs;

import java.io.InputStream;

public class CsvReaderConvert extends PoiReaderAnalysis {
    public CsvReaderConvert(InputStream is) {
        if (super.isSupport(is))
            throw new NullPointerException("no support InputStream");
    }

    @Override
    public final boolean isSupport(InputStream is) {
        return true;
    }
}
