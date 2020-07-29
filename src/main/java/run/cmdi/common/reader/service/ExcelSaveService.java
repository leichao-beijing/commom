package run.cmdi.common.reader.service;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmdi.common.reader.core.ConvertDataToWorkbook;
import run.cmdi.common.reader.core.WorkbookInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExcelSaveService implements WorkbookInfo {
    @Getter
    private Workbook workbook;
    private final List<ConvertDataToWorkbook> list = new ArrayList<ConvertDataToWorkbook>();
    private boolean state = false;
    private InputStream is;

    public ExcelSaveService() {
    }

    public ExcelSaveService(InputStream is) {
        this.is = is;
    }

    /**
     * @param state true HSSF false Excel97-2003;XSSF Excel2007
     */
    public ExcelSaveService(boolean state) {
        this.state = state;
    }

    /**
     * @param sheetName
     * @param t
     */
    public <T> ConvertDataToWorkbook buildConvert(String sheetName, Class<T> t) {
        ConvertDataToWorkbook convert = new ConvertDataToWorkbook(this, sheetName, t);
        list.add(convert);
        return convert;
    }

    /**
     * @param sheetName
     * @param t
     * @param exceptFieldName 输出表格除外的数据对象FieldName
     */
    public <T> ConvertDataToWorkbook buildConvert(String sheetName, Class<T> t, Set<String> exceptFieldName) {
        ConvertDataToWorkbook convert = buildConvert(sheetName, t);
        exceptFieldName.forEach((val) -> {
            convert.getMap().remove(val);
        });
        return convert;
    }

    @Override
    public void save(OutputStream out) throws IOException {
        boolean state = false;
        for (ConvertDataToWorkbook convertDataToWorkbook : list) {
            if (convertDataToWorkbook.isState()) {
                state = true;
                break;
            }
        }
        if (state) {
            workbook.write(out);
        } else
            throw new IOException("没有添加数据无法进行保存");
    }

    @Override
    public void close() {
        try {
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            return;
        }
    }

    @Override
    public Workbook createWorkbook() throws IOException {
        if (workbook == null)
            if (is != null)
                workbook = WorkbookFactory.create(is);
            else
                workbook = WorkbookFactory.create(state);
        return workbook;
    }

}