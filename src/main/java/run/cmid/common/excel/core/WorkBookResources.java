package run.cmid.common.excel.core;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkBookResources implements BookResources {
   private final Workbook workbook;
    public WorkBookResources(Workbook workbook) {
        this.workbook=workbook;
    }

    @Override
    public ReadBook book(int index) {
        Sheet sheet = workbook.getSheetAt(index);
        return new SheetReadBook(sheet,"index:"+index);
    }

    @Override
    public ReadBook book(String tag) {
        Sheet sheet = workbook.getSheet(tag);
        return new SheetReadBook(sheet,tag);
    }

    @Override
    public List<ReadBook> bookList() {
        Iterator<Sheet> it = workbook.sheetIterator();
        List<ReadBook> list = new ArrayList<ReadBook>();
        while(it.hasNext())
            list.add(new SheetReadBook(it.next(),null));
        return list;
    }
}
