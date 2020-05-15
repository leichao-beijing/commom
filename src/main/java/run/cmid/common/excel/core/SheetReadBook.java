package run.cmid.common.excel.core;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class SheetReadBook implements  ReadBook{
    private  final  Sheet sheet;
    public SheetReadBook(Sheet sheet,String tagName){
        if(sheet==null)
            throw new NullPointerException((tagName==null)?"":tagName+"no find");
        this.sheet=sheet;
    }
    @Override
    public List<Object> readRow(int rowNum) {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }
}
