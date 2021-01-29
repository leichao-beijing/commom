package run.cmdi.common.convert;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;

public class WriterPoi implements WriterEntity<String> {
    public WriterPoi(boolean xssf, String newSheetName) throws IOException {
        this.workbook = WorkbookFactory.create(xssf);
        this.sheet = workbook.createSheet(newSheetName);
    }

    public WriterPoi(Sheet sheet) {
        this.sheet = sheet;
        this.workbook = sheet.getWorkbook();
    }

//    public WriterPoi(File file, String newSheetName) throws IOException {
//        this.workbook = WorkbookFactory.create(file);
//        existsSheetName(newSheetName);
//        this.sheet = workbook.createSheet(newSheetName);
//    }
//
//    public WriterPoi(InputStream is, String newSheetName) throws IOException {
//        this.workbook = WorkbookFactory.create(is);
//        existsSheetName(newSheetName);
//        this.sheet = workbook.createSheet(newSheetName);
//    }

    public static void existsSheetName(Workbook workbook, String newSheetName) throws IOException {
        Sheet value = workbook.getSheet(newSheetName);
        if (value != null)
            throw new IOException("sheetName:" + newSheetName + " is exists");
    }


    private final Sheet sheet;
    private final Workbook workbook;

    @Override
    public void add(int rownum, int column, Object value, String s) {
        Row row = sheet.getRow(rownum);
        if (row == null)
            row = sheet.createRow(rownum);
        Cell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        cell.setCellValue(value.toString());
        //todo 将value的值写入 cell 中
    }

    @Override
    public void save(OutputStream os) throws IOException {
        sheet.getWorkbook().write(os);
    }

    @Override
    public void close() throws IOException {
        sheet.getWorkbook().close();
    }
}