package run.cmdi.common.convert.plugs;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvUtil;
import org.apache.commons.io.output.WriterOutputStream;
import org.mozilla.universalchardet.UniversalDetector;
import run.cmdi.common.convert.BuildPage;
import run.cmdi.common.convert.ReaderPageInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderAnalysis implements ReaderPageInterface<InputStream>, BuildPage {
    @Override
    public ConvertOutPageCsv buildPage() {
        return new ConvertOutPageCsv(csvData);
    }

    @Override
    public ConvertOutPageCsv buildPage(String pageName) {
        throw new NullPointerException(" csv not Support " + pageName);
    }

    @Override
    public ConvertOutPageCsv buildPage(Integer pageIndex) {
        return buildPage();
    }

    @Override
    public List<ConvertOutPageCsv> buildPageList() {
        List<ConvertOutPageCsv> list = new ArrayList<>();
        list.add(buildPage());
        return list;
    }

    @Override
    public int size() {
        return csvData.getRowCount();
    }

    @Override
    public String getRegisterName() {
        return "csv";
    }

    private InputStreamReader isr;
    private CsvData csvData;

    @Override
    public boolean isSupport(InputStream is) {
        try {
            String code = UniversalDetector.detectCharset(is);
            if (code == null)
                return false;
            is.reset();
            this.isr = new InputStreamReader(is, code);
            this.csvData = CsvUtil.getReader().read(isr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
