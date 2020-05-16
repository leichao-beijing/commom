package run.cmid.common.reader.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import run.cmid.common.compare.model.CompareResponse;

/**
 * 
 * @author leichao
 * @date 2019年12月3日--上午9:54:02
 */
public class SheetUtils {

    /**
     * 获取cell
     */
    public static Optional<Cell> getCell(Sheet sheet, int rownum, int column) {
        Optional<Row> rowOptional = SheetUtils.getRow(sheet, rownum);
        return Optional.ofNullable(rowOptional).filter(a -> !a.isEmpty()).map(b -> b.get())
                .map(cell -> cell.getCell(column));
    }

    /**
     * 获取指定行的范围列的内容，写入List columStart<columEnd 升序 columStart>columEnd 降序
     */
    public static List<String> getRowRangeCell(Row row, int columStart, int columEnd) {
        ArrayList<String> list = new ArrayList<String>();
        if (columStart < columEnd) {// 升序
            for (int i = columStart; i <= columEnd; i++) {
                if (row.getCell(i) != null)
                    list.add(row.getCell(i).toString());
                else
                    list.add("");
            }
        } else {// 降序
            for (int i = columStart; i >= columEnd; i--) {
                if (row.getCell(i) != null)
                    list.add(row.getCell(i).toString());
                else
                    list.add("");
            }
        }
        return list;
    }

    /**
     * 获取cell
     */
    public static Optional<Cell> getCell(Sheet sheet, CellAddress address) {
        return getCell(sheet, address.getRow(), address.getColumn());
    }

    /**
     * 获取row
     */
    public static Optional<Row> getRow(Sheet sheet, int rownum) {
        return Optional.ofNullable(sheet).map(r -> sheet.getRow(rownum));
    }

    public static Row getCreateRow(Sheet sheet, int rownum) {
        Optional<Row> rowOption = getRow(sheet, rownum);
        if (rowOption.isEmpty())
            return sheet.createRow(rownum);
        return rowOption.get();
    }

    /**
     * 获取单元格，不存在时，创建单元格
     */
    public static Cell getCreateCell(Sheet sheet, int rownum, int column) {
        Optional<Row> rowOption = getRow(sheet, rownum);
        Row row = null;
        if (rowOption.isEmpty())
            row = sheet.createRow(rownum);
        else
            row = rowOption.get();
        Cell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        return cell;
    }

    public static Cell getCreateCell(Row row, int column) {
        Cell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        return cell;
    }

    /**
     * 获取最大行数
     */
    public static int getRowCount(Sheet sheet) {
        int rownum = sheet.getLastRowNum();
        if (sheet.getRow(rownum) == null)
            return rownum;
        return rownum + 1;
    }

    /**
     * 获取当前行最大列数
     */
    public static int getColumCount(Sheet sheet, int rownum) {
        if (sheet.getRow(rownum) == null)
            return 0;
        return sheet.getRow(rownum).getLastCellNum();
    }

    /**
     * 获取sheet内现存最大行数，匹配空白行除外
     */
    public static <A, B> int sheetCount(Sheet sheet, List<CompareResponse<A, B>> list) {
        int size = sheet.getLastRowNum();
        while (isRowAllEmpty(sheet.getRow(size), list)) {
            size--;
        }
        return size + 1;
    }

    /**
     * 当前行与匹配结果全部为空时，才认为该行为空行
     */
    public static <A, B> boolean isRowAllEmpty(Function<Integer, String> fun, List<CompareResponse<A, B>> list) {
        int size = 0;
        for (CompareResponse<?, ?> compareResponse : list) {
            if (compareResponse.getDesIndex() == -1) {
                size++;
                continue;
            }
            String val = fun.apply(compareResponse.getDesIndex());
            if (val == null || val.toString().equals(""))
                size++;
        }
        return list.size() == size ? true : false;
    }

    /**
     * 当前行与匹配结果全部为空时，才认为该行为空行
     */
    public static <A, B> boolean isRowAllEmpty(Row row, List<CompareResponse<A, B>> list) {
        if (row == null)
            return true;
        return isRowAllEmpty((i) -> {
            Cell cell = row.getCell(i);
            if (cell == null)
                return null;
            return cell.toString().trim();
        }, list);
    }

    /**
     * 将sheet内合并单元格信息提取并保存至map文件中，便于数据检索
     */
    public static HashMap<CellAddress, CellAddress> computeRangeCellMap(Sheet sheet) {
        List<CellRangeAddress> list = sheet.getMergedRegions();
        HashMap<CellAddress, CellAddress> map = new HashMap<CellAddress, CellAddress>();
        for (CellRangeAddress cellRangeAddress : list) {
            CellAddress cellAddress = new CellAddress(cellRangeAddress.getFirstRow(),
                    cellRangeAddress.getFirstColumn());
            Iterator<CellAddress> it = cellRangeAddress.iterator();
            while (it.hasNext()) {
                CellAddress next = it.next();
                map.put(next, cellAddress);
            }
        }
        return map;
    }

    /**
     * 复制sheet ,同时复制样式
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet) {
        DataFormat dataFormat = desSheet.getWorkbook().createDataFormat();
        CellStyle desStyle = desSheet.getWorkbook().createCellStyle();
        int rowCount = SheetUtils.getRowCount(srcSheet);
        for (int rownum = 0; rownum < rowCount; rownum++) {// row
            int columCount = SheetUtils.getColumCount(srcSheet, rownum);
            for (int column = 0; column < columCount; column++) {// column
                Optional<Cell> cellOptional = getCell(srcSheet, rownum, column);
                if (cellOptional.isEmpty())
                    continue;
                Cell srcCell = cellOptional.get();
                desStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Cell desCell = SheetUtils.getCreateCell(desSheet, rownum, column);
                copyCellValue(srcCell, srcCell.getCellType(), desCell, dataFormat);
                // desCell.setCellStyle(desCell.getSheet().getWorkbook().createCellStyle());
                // BorderMode borderMode =
                // SheetStyleUtils.getBorderMode(srcCell.getCellStyle());
                // java.awt.Color color = SheetStyleUtils.getColor(srcCell.getCellStyle());
                // SheetStyleUtils.setColor(desCell, color, borderMode);

                desCell.getCellStyle().setDataFormat(srcCell.getCellStyle().getDataFormat());
            }
        }

        SheetStyleUtils.copyColumnWidth(srcSheet, desSheet);
    }

    public static void copyCellValue(Cell srcCell, CellType type, Cell desCell, DataFormat dataFormat) {

        switch (type) {
        case STRING:
            desCell.setCellValue(srcCell.getStringCellValue());
            break;
        case NUMERIC:
            desCell.setCellValue(srcCell.getNumericCellValue());
            break;
        case BOOLEAN:
            desCell.setCellValue(srcCell.getBooleanCellValue());
            break;
        case FORMULA:
            copyCellValue(srcCell, srcCell.getCachedFormulaResultType(), desCell, dataFormat);
            break;
        case BLANK:
            break;
        case _NONE:
            break;
        case ERROR:
            break;
        }
    }
}
