package run.cmdi.common.convert.plugs;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmdi.common.convert.ConvertPage;
import run.cmdi.common.convert.TypeAnalysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TypeAnalysisWorkBook implements TypeAnalysis<InputStream> {
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
    public ConvertPage buildPage() {
        return new ConvertPageSheet(workbook.getSheetAt(workbook.getActiveSheetIndex()));
    }

    @Override
    public ConvertPage buildPage(String pageName) {
        return new ConvertPageSheet(workbook.getSheet(pageName));
    }

    @Override
    public ConvertPage buildPage(Integer pageIndex) {
        return new ConvertPageSheet(workbook.getSheetAt(pageIndex));
    }

    @Override
    public List<ConvertPage> buildPageList() {
        List<ConvertPage> list = new ArrayList<>();
        workbook.forEach(sheet -> {
            list.add(new ConvertPageSheet(sheet));
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
