package run.cmdi.common.convert.plugs;

import cn.hutool.core.date.DateTime;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.SheetUtil;
import run.cmdi.common.convert.ConvertOutPage;
import run.cmdi.common.poi.core.SheetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ConvertOutPageSheet implements ConvertOutPage<List>{
    @Getter
    private final Sheet sheet;
    @Getter
    private final HashMap<CellAddress, CellAddress> cellRangeMap;

    public ConvertOutPageSheet(Sheet sheet) {
        this.sheet = sheet;
        this.cellRangeMap = SheetUtils.computeRangeCellMap(sheet);
    }

    @Override
    public List getValues(Integer index) {
        Row row = sheet.getRow(index);
        return getValue(row);
    }

    private List getValue(Row row) {
        List<Object> list = new ArrayList<>();
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

    @Override
    public List<List> getAll() {
        Iterator<Row> it = sheet.rowIterator();
        List<List> list = new ArrayList<>();
        it.forEachRemaining(row -> {
            list.add(getValue(row));
        });
        return list;
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
                //if (cellRangeMap != null) {
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
                //}
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
    public int size() {
        return sheet.getLastRowNum();
    }
}
