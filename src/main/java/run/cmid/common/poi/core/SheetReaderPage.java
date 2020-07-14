package run.cmid.common.poi.core;

import cn.hutool.core.date.DateTime;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.SheetUtil;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTagError;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.reader.core.ReaderPage;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SheetReaderPage implements ReaderPage<Sheet, Cell> {
    private final Sheet sheet;
    private final HashMap<CellAddress, CellAddress> cellRangeMap;

    public SheetReaderPage(Sheet sheet, String tagName, ReaderPoiConfig readerPoiConfig) {
        if (sheet == null)
            throw new NullPointerException((tagName == null) ? "" : tagName + " no find");
        this.sheet = sheet;
        if (readerPoiConfig.isCellRangeState())
            this.cellRangeMap = SheetUtils.computeRangeCellMap(sheet);
        else
            this.cellRangeMap = null;
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
                    return cell.getDateCellValue();
                else {
                    if (cell.getCellStyle().getDataFormat() == 0 || cell.getCellStyle().getDataFormat() == 1)
                        return (int) cell.getNumericCellValue();
                    return cell.getNumericCellValue();
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
    public void info(HeadInfo headInfo) throws ConverterExcelException {
        this.length = SheetUtils.sheetCount(sheet, headInfo.getMap());// 获取最大行
//        Map<ConverterErrorType, String> errorType = computeErrorType(headInfo.getResponse().getErrorList());
//        if (errorType.size() != 0)
//            throw new ConverterExcelException(errorType);
        this.headInfo = headInfo;
    }

    private HeadInfo headInfo;
    private int length = -1;

    @Override
    public int length() {
        return length;
    }

    private Map<ConverterErrorType, String> computeErrorType(
            List<LocationTagError<FieldDetail, ConverterExcelException>> columnErrorList) {
        EnumMap<ConverterErrorType, String> errorTypeMap = new EnumMap<ConverterErrorType, String>(
                ConverterErrorType.class);
        for (LocationTagError<FieldDetail, ConverterExcelException> locationTagError : columnErrorList) {
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
