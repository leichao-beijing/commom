package run.cmid.common.reader.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import run.cmid.common.reader.core.BookResources;
import run.cmid.common.reader.core.ReaderPage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkbookResources implements BookResources {
   private final Workbook workbook;
   private  final  boolean cellRangeState;
    public WorkbookResources(Workbook workbook, boolean cellRangeState) {
        this.workbook=workbook;
        this.cellRangeState=cellRangeState;
    }

    @Override
    public ReaderPage book(int index) {
        Sheet sheet = workbook.getSheetAt(index);
        return new SheetReaderPage(sheet,"index:"+index,cellRangeState);
    }

    @Override
    public ReaderPage book(String tag) {
        Sheet sheet = workbook.getSheet(tag);
        return new SheetReaderPage(sheet,tag,cellRangeState);
    }

    @Override
    public List<ReaderPage> bookList() {
        Iterator<Sheet> it = workbook.sheetIterator();
        List<ReaderPage> list = new ArrayList<ReaderPage>();
        while(it.hasNext())
            list.add(new SheetReaderPage(it.next(),null,cellRangeState));
        return list;
    }
}
