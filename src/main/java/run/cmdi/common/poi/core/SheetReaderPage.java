package run.cmdi.common.poi.core;

import cn.hutool.core.date.DateTime;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.SheetUtil;
import run.cmdi.common.compare.model.LocationTagError;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.core.ReaderPage;
import run.cmdi.common.reader.model.FieldDetailOld;
import run.cmdi.common.reader.model.HeadInfo;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.exception.ConverterExcelException;

import java.util.*;

public class SheetReaderPage implements ReaderPage<Sheet, Cell> {
    private final Sheet sheet;
    private final HashMap<CellAddress, CellAddress> cellRangeMap;

    public SheetReaderPage(Sheet sheet, String tagName, ReaderPoiConfig readerPoiConfig) {
        if (sheet == null)
            throw new NullPointerException((tagName == null) ? "" : tagName + " no find");
        this.sheet = sheet;
        this.cellRangeMap = SheetUtils.computeRangeCellMap(sheet);
        this.length = sheet.getLastRowNum();
    }

    @Override
    public Sheet getPage() {
        return sheet;
    }

    @Override
    public List<Object> readRowList(int rowNum) {
        List<Object> list = new ArrayList<>();
        Row row = sheet.getRow(rowNum);
        if (row == null)
            return null;
        short lastNum = row.getLastCellNum();
        for (int i = 0; i < lastNum; i++) {
            Cell value = row.getCell(i);
            if (value == null) {
                list.add(null);
                continue;
            }
            list.add(getCellValue(value, value.getCellType()));
        }
        return (list.size() == 0) ? null : list;
    }

    private Object getCellValue(Cell cell, CellType type) {
        switch (type) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return new DateTime(cell.getDateCellValue());
                else {
                    int valueInteger = (int) cell.getNumericCellValue();
                    double valueDouble = cell.getNumericCellValue();
                    if (valueInteger == Integer.MAX_VALUE || valueInteger == Integer.MIN_VALUE) {
                        long valueLong = (long) cell.getNumericCellValue();
                        if (valueDouble == valueLong) {
                            return valueLong;
                        }
                        return valueDouble;
                    }
                    if (valueInteger == valueDouble)
                        return valueInteger;
                    return valueDouble;
                }

            case STRING:
                return cell.toString();
            case FORMULA:
                CellType formulaType = cell.getCachedFormulaResultType();
                if (formulaType != CellType.FORMULA) {
                    return getCellValue(cell, formulaType);
                }
                return null;
            case BLANK:
                if (cellRangeMap != null) {
                    CellAddress cellRangeAddress = cellRangeMap.get(cell.getAddress());
                    if (cellRangeAddress != null) {
                        cell = SheetUtil.getCellWithMerges(cell.getSheet(), cellRangeAddress.getRow(),
                                cellRangeAddress.getColumn());
                        if (cell == null)
                            type = CellType.ERROR;
                        else {
                            if (cell.getCellType() == CellType.BLANK)
                                type = CellType.ERROR;
                            else
                                type = cell.getCellType();
                        }
                        return getCellValue(cell, type);
                    }
                }
                return null;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case _NONE:
            case ERROR:
                return null;
        }
        return null;
    }


    @Override
    public void info(HeadInfo headInfo) {
        this.length = SheetUtils.sheetCount(sheet, headInfo.getMap());// 获取最大行
        this.headInfo = headInfo;
    }

    private HeadInfo headInfo;
    private int length = -1;

    @Override
    public int length() {
        return length;
    }

    private Map<ConverterErrorType, String> computeErrorType(
            List<LocationTagError<FieldDetailOld, ConverterExcelException>> columnErrorList) {
        EnumMap<ConverterErrorType, String> errorTypeMap = new EnumMap<ConverterErrorType, String>(
                ConverterErrorType.class);
        for (LocationTagError<FieldDetailOld, ConverterExcelException> locationTagError : columnErrorList) {
            if (errorTypeMap.get(locationTagError.getEx().getType()) == null) {
                errorTypeMap.put(locationTagError.getEx().getType(), locationTagError.getEx().getMessage());
                continue;
            }
            errorTypeMap.put(locationTagError.getEx().getType(), errorTypeMap.get(locationTagError.getEx().getType())
                    + " " + locationTagError.getEx().getMessageValue());
        }
        return errorTypeMap;
    }

    @Override
    public List<Cell> readRowUnit(int rowNum) {
        List<Cell> list = new ArrayList<>();
        Row row = sheet.getRow(rowNum);
        if (row == null)
            return null;
        short lastNum = row.getLastCellNum();
        for (int i = 0; i < lastNum; i++) {
            Cell value = row.getCell(i);
            if (value == null) {
                list.add(null);
                continue;
            }
            list.add(value);
        }
        return (list.size() == 0) ? null : list;
    }
}
