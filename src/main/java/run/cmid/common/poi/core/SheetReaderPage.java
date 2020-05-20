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
import run.cmid.common.reader.model.eumns.ExcelExceptionType;

import java.text.SimpleDateFormat;
import java.util.*;

public class SheetReaderPage implements ReaderPage<Sheet,Cell> {
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
        this.length=sheet.getLastRowNum();
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
                short formatIndex = cell.getCellStyle().getDataFormat();
               //int i = cell.getColumnIndex();
                if(headInfo==null)
                    return  cell.getNumericCellValue();
                List<CompareResponse<FieldDetail, String>> list = headInfo.getResponse().getList();
                FieldDetail detail = null;
                for (CompareResponse<FieldDetail, String> fieldDetailStringCompareResponse : list) {
                    if (fieldDetailStringCompareResponse.getDesIndex() == cell.getColumnIndex()) {
                        detail = fieldDetailStringCompareResponse.getSrcData();
                        break;
                    }
                }
                if (detail == null)
                    return null;
                Class<?> classType = detail.getField().getType();
                if (!classType.getName().startsWith("java.time") && !classType.equals(Date.class))
                    return cell.getNumericCellValue();
                String da = sheet.getWorkbook().createDataFormat().getFormat(formatIndex);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(da);
                    DateTime dateTime = new DateTime(DateUtil.getJavaDate(cell.getNumericCellValue(), sdf.getTimeZone()));
                    return dateTime;
                } catch (IllegalArgumentException e) {
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
        this.headInfo = headInfo;
    }

    private HeadInfo headInfo;
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
