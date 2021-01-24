package run.cmdi.common.poi.core;

import org.apache.poi.ss.usermodel.*;
import run.cmdi.common.convert.plugs.PoiReaderConvert;

import java.io.*;
import java.util.Iterator;

public class PoiReaderBuild extends StylePalette implements PageClone<Workbook> {

    public static PoiReaderBuild build(PoiReaderConvert poiReaderConvert) throws IOException {
        PoiReaderBuild poiReader = new PoiReaderBuild(poiReaderConvert);
        return poiReader;
    }

    private PoiReaderBuild(PoiReaderConvert poiReaderConvert) {
        super(poiReaderConvert.getWorkbook());
        this.poiReaderConvert = poiReaderConvert;
    }

    private final PoiReaderConvert poiReaderConvert;

    /**
     * 对象本身的workbook内的tagName 复制到方法传入的workbook内新tagNew内
     *
     * @param workBookDes
     * @param tag
     * @param tagNew
     */
    @Override
    public void clone(Workbook workBookDes, String tag, String tagNew) {
        if (getWorkbook().equals(workBookDes)) {
            Sheet srcSheet = getWorkbook().getSheet(tag);
            Sheet sheet = getWorkbook().cloneSheet(getWorkbook().getSheetIndex(srcSheet));
            int index = getWorkbook().getSheetIndex(sheet);
            if (tagNew != null && tagNew.length() != 0)
                getWorkbook().setSheetName(index, tagNew);
            return;
        }
        Sheet sheet = workBookDes.getSheet(tagNew);
        if (sheet != null)
            workBookDes.removeSheetAt(workBookDes.getSheetIndex(sheet));
        sheet = workBookDes.createSheet(tagNew);
        Sheet sheetSec = getWorkbook().getSheet(tag);
        copySheet(sheetSec, sheet, new StylePalette(workBookDes));
    }

    private void copySheet(Sheet sheetSec, Sheet sheetDes, StylePalette stylePaletteDes) {
        Iterator<Row> it = sheetSec.iterator();
        while (it.hasNext()) {
            Row rowSec = it.next();
            Row rowDes = sheetDes.createRow(rowSec.getRowNum());
            copyRow(rowSec, rowDes, stylePaletteDes);
        }
    }

    private void copyRow(Row rowSec, Row rowDes, StylePalette stylePaletteDes) {
        Iterator<Cell> it = rowSec.cellIterator();
        while (it.hasNext()) {
            Cell cellSec = it.next();
            Cell cellDes = rowDes.createCell(cellSec.getColumnIndex());
            SheetUtils.copyCellValue(cellSec, cellDes, stylePaletteDes);
        }
    }
}
