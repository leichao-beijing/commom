package run.cmdi.common.convert.plugs;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmdi.common.convert.ReaderPageInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PoiReaderAnalysis implements ReaderPageInterface<InputStream> {
    @Getter
    @Setter
    private Workbook workbook;

    @Override
    public boolean isSupport(InputStream is) {
        try {
            this.workbook = WorkbookFactory.create(is);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ConvertOutPageSheet buildPage() {
        return new ConvertOutPageSheet(workbook.getSheetAt(workbook.getActiveSheetIndex()));
    }

    @Override
    public ConvertOutPageSheet buildPage(String pageName) {
        return new ConvertOutPageSheet(workbook.getSheet(pageName));
    }

    @Override
    public ConvertOutPageSheet buildPage(Integer pageIndex) {
        return new ConvertOutPageSheet(workbook.getSheetAt(pageIndex));
    }

    @Override
    public List<ConvertOutPageSheet> buildPageList() {
        List<ConvertOutPageSheet> list = new ArrayList<>();
        workbook.forEach(sheet -> {
            list.add(new ConvertOutPageSheet(sheet));
        });
        return list;
    }

    @Override
    public int size() {
        return workbook.getAllNames().size();
    }

    @Override
    public String getRegisterName() {
        return "Workbook";
    }


    @Override
    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
