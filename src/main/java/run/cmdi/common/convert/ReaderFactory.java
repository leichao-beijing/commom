package run.cmdi.common.convert;

import run.cmdi.common.convert.plugs.CsvReaderAnalysis;
import run.cmdi.common.convert.plugs.PoiReaderAnalysis;

import java.io.InputStream;

public final class ReaderFactory extends ReaderFactoryAbstract<InputStream, ReaderPageInterface<InputStream>> {
    /**
     * 注册默认的转换器
     */
    private void defaultRegister() {
        register(PoiReaderAnalysis.class);//poi注入
        register(CsvReaderAnalysis.class);//csv注入
    }

    public ReaderFactory(InputStream is, String tagName) {
        super(is);
        defaultRegister();
        if ((this.analysis = getRegister(tagName)) == null)
            throw new NullPointerException("no register tagName:" + tagName + " TypeAnalysis");
    }

    public ReaderFactory(InputStream is) {
        super(is);
        defaultRegister();
        if ((this.analysis = getRegister()) == null)
            throw new NullPointerException("no register TypeAnalysis");
    }

    public final ReaderPageInterface buildAnalysis() {
        return analysis;
    }

    private ReaderPageInterface analysis;
}
