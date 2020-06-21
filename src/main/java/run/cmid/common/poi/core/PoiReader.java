package run.cmid.common.poi.core;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;

import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.reader.core.BookPage;
import run.cmid.common.reader.core.ReaderPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PoiReader extends StylePalette implements BookPage<Workbook, Sheet, Cell>, PageClone<PoiReader> {
    @Getter
    private final Workbook workbook;
    private final ReaderPoiConfig readerPoiConfig;
    private final File outFile;

    public static PoiReader build(InputStream is, ReaderPoiConfig readerPoiConfig) throws IOException {
        Workbook workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is));
        PoiReader poiReader = new PoiReader(workbook, readerPoiConfig, null);
        return poiReader;
    }

    public static PoiReader build(InputStream is) throws IOException {
        Workbook workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is));
        PoiReader poiReader = new PoiReader(workbook, new ReaderPoiConfig(), null);
        return poiReader;
    }

    public static PoiReader build(InputStream is, String password, ReaderPoiConfig readerPoiConfig, File outFile) throws IOException {
        Workbook workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is), password);
        return new PoiReader(workbook, readerPoiConfig, outFile);
    }

    public static PoiReader build(Workbook workbook, ReaderPoiConfig readerPoiConfig, File outFile) {
        return new PoiReader(workbook, readerPoiConfig, outFile);
    }


    private PoiReader(Workbook workbook, ReaderPoiConfig readerPoiConfig, File outFile) {
        super(workbook);
        this.workbook = workbook;
        this.readerPoiConfig = readerPoiConfig;
        this.outFile = outFile;
    }

    @Override
    public Workbook getResources() {
        return workbook;
    }

    @Override
    public ReaderPage<Sheet, Cell> book(int index) {
        Sheet sheet = workbook.getSheetAt(index);
        return new SheetReaderPage(sheet, "index:" + index, readerPoiConfig);
    }

    @Override
    public ReaderPage<Sheet, Cell> book(String tag) {
        Sheet sheet = workbook.getSheet(tag);
        return new SheetReaderPage(sheet, tag, readerPoiConfig);
    }

    @Override
    public ReaderPage<Sheet, Cell> book(Sheet sheet) {
        return new SheetReaderPage(sheet, sheet.getSheetName(), readerPoiConfig);
    }

    @Override
    public List<ReaderPage<Sheet, Cell>> bookList() {
        Iterator<Sheet> it = workbook.sheetIterator();
        List<ReaderPage<Sheet, Cell>> list = new ArrayList<ReaderPage<Sheet, Cell>>();
        while (it.hasNext())
            list.add(new SheetReaderPage(it.next(), null, readerPoiConfig));
        return list;
    }

    /**
     * 文件保存至outFile内。关闭workbook和FileOutputStream
     */
    public void saveAndClose() throws IOException {
        FileOutputStream fos = new FileOutputStream(outFile);
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    /**
     * 文件保存至file内。关闭workbook和FileOutputStream
     *
     * @param file
     */
    public void saveAndClose(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public void close() {
        try {
            workbook.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void clone(PoiReader resources, String tag, String tagNew) {
        if (equals(resources)) {
            Sheet srcSheet = workbook.getSheet(tag);
            Sheet sheet = workbook.cloneSheet(workbook.getSheetIndex(srcSheet));
            int index = workbook.getSheetIndex(sheet);
            if (tagNew != null && tagNew.length() != 0)
                workbook.setSheetName(index, tagNew);
            return;
        }
        ReaderPage<Sheet, Cell> srcBook = book(tag);

        Sheet sheet = resources.getResources().getSheet(tagNew);
        if (sheet != null)
            resources.getResources().removeSheetAt(resources.getResources().getSheetIndex(sheet));
        sheet = resources.getResources().createSheet(tagNew);
        for (int i = 0; i < srcBook.length(); i++) {
            List<Cell> list = srcBook.readRowUnit(i);
            if (list != null) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = list.get(j);
                    if (cell != null) {
                        Cell desCell = row.createCell(j);
                        SheetUtils.copyCellValue(cell, desCell, resources);
                    }
                }
            }
        }
    }

    /**
     * 创建sheet，当sheet存在时，删除原sheet继续创建
     */
    public static Sheet createSheetRemove(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            if (sheet.getPhysicalNumberOfRows() == 0)
                return sheet;
            int size = sheet.getPhysicalNumberOfRows();
            for (int i = size - 1; i >= 0; i--) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                sheet.removeRow(row);
            }
            return sheet;
        }
        return workbook.createSheet(sheetName);
    }
}
