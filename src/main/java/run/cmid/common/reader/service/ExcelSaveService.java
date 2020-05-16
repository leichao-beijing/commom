package run.cmid.common.reader.service;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmid.common.reader.core.ConvertDataToWorkbook;
import run.cmid.common.reader.core.WorkbookInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelSaveService implements WorkbookInfo {
    public ExcelSaveService(File outFile) throws IOException {
        this.outFile = outFile;
    }

    private File outFile;
    private FileOutputStream out;
    private Workbook workbook;
    private final List<ConvertDataToWorkbook<?>> list = new ArrayList<ConvertDataToWorkbook<?>>();

    public <T> ConvertDataToWorkbook<T> buildConvert(String sheetName, Class<T> t) throws IOException {
        ConvertDataToWorkbook<T> convert = new ConvertDataToWorkbook<T>(this, sheetName, t);
        list.add(convert);
        return convert;
    }

    public void save() throws IOException {
        boolean state = false;
        for (ConvertDataToWorkbook<?> convertDataToWorkbook : list) {
            if (convertDataToWorkbook.isState()) {
                state = true;
                break;
            }
        }
        if (state) {
            System.err.println("已将数据保存至：" + outFile.getPath());
            workbook.write(out);
        }
    }

    public void close() {
        IoUtil.close(out);
        try {
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            // e.printStackTrace();
            return;
        }

    }

    @Override
    public void createFile() throws IOException {
        if (out == null)
            this.out = new FileOutputStream(outFile);

    }

    @Override
    public Workbook createWorkbook() throws IOException {
        if (workbook == null)
            this.workbook = WorkbookFactory.create(true);
        return workbook;
    }
}