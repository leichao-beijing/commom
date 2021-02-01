package run.cmdi.common.convert;

import cn.hutool.core.util.NumberUtil;
import org.apache.poi.ss.usermodel.*;
import run.cmdi.common.convert.model.WriterFieldInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class WriterPoi implements WriterEntity<WriterFieldInfo> {
    public WriterPoi(boolean xssf, String newSheetName) throws IOException {
        this.workbook = WorkbookFactory.create(xssf);
        this.sheet = workbook.createSheet(newSheetName);
    }

    public WriterPoi(Sheet sheet) {
        this.sheet = sheet;
        this.workbook = sheet.getWorkbook();
    }

//    public static void existsSheetName(Workbook workbook, String newSheetName) throws IOException {
//        Sheet value = workbook.getSheet(newSheetName);
//        if (value != null)
//            throw new IOException("sheetName:" + newSheetName + " is exists");
//    }


    private final Sheet sheet;
    private final Workbook workbook;

    @Override
    public void add(int rownum, int column, Object value, WriterFieldInfo info) {
        if (value == null)
            return;
        Row row = sheet.getRow(rownum);
        if (row == null)
            row = sheet.createRow(rownum);
        Cell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        if (value.getClass().isAssignableFrom(Date.class)) {
            cell.setCellValue((Date) value);
            if (info.getFormatDate() != null)
                cell.setCellFormula(info.getFormatDate());
        } else if (NumberUtil.isNumber(value.toString())) {
            cell.setCellValue(NumberUtil.add(value.toString()).doubleValue());
            if (info.getFormatDate() != null)
                cell.setCellFormula(info.getFormatDate());
        } else
            cell.setCellValue(value.toString());
        //todo 将value的值写入 cell 中
    }

    @Override
    public void add(int column, Object value, WriterFieldInfo info) {
        add(getCount(), column, value, info);
    }

    @Override
    public Integer getCount() {
        return sheet.getLastRowNum();
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