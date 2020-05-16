package run.cmid.common.reader.service;

import cn.hutool.core.date.DateTime;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.SheetUtil;
import run.cmid.common.compare.model.LocationTagError;
import run.cmid.common.reader.core.ReaderPage;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.eumns.ExcelExceptionType;
import run.cmid.common.reader.plugins.SheetUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class SheetReaderPage implements ReaderPage {
    private final Sheet sheet;
    private final HashMap<CellAddress, CellAddress> cellRangeMap;

    public SheetReaderPage(Sheet sheet, String tagName, boolean cellRangeState) {
        if (sheet == null)
            throw new NullPointerException((tagName == null) ? "" : tagName + " no find");
        this.sheet = sheet;
        if (cellRangeState)
            this.cellRangeMap = SheetUtils.computeRangeCellMap(sheet);
        else
            this.cellRangeMap = null;
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
                short formatIndex = cell.getCellStyle().getDataFormat();
                if(formatIndex==0)
                    return cell.getNumericCellValue();
                String da = sheet.getWorkbook().createDataFormat().getFormat(formatIndex);
                SimpleDateFormat sdf =new SimpleDateFormat(da);
                DateTime dateTime=  new DateTime(DateUtil.getJavaDate(cell.getNumericCellValue(), sdf.getTimeZone()));
                return dateTime;
            case STRING:
                return cell.toString();
            case FORMULA:
                CellType formulaType = cell.getCachedFormulaResultType();
                if (formulaType == CellType.FORMULA) {
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
                        return getCellValue(cell, cell.getCellType());
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
        this.length = SheetUtils.sheetCount(sheet, headInfo.getResponse().getList());// 获取最大行
        Map<ExcelExceptionType, String> errorType = computeErrorType(headInfo.getResponse().getErrorList());
        if (errorType.size() != 0)
            throw new ConverterExcelException(errorType);
    }

    private int length = -1;

    @Override
    public int length() {
        return length;
    }

    private Map<ExcelExceptionType, String> computeErrorType(
            List<LocationTagError<FieldDetail, ConverterExcelException>> columnErrorList) {
        EnumMap<ExcelExceptionType, String> errorTypeMap = new EnumMap<ExcelExceptionType, String>(
                ExcelExceptionType.class);
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
}
