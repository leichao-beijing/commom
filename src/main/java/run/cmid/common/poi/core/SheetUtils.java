package run.cmid.common.poi.core;

import java.util.*;
import java.util.function.Function;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.reader.model.to.BorderMode;

/**
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
     * 获取指定行的范围列的内容，写入List columnStart<columnStart 升序 columnStart>columnEnd 降序
     */
    public static List<String> getRowRangeCell(Row row, int columnStart, int columnEnd) {
        ArrayList<String> list = new ArrayList<String>();
        if (columnStart < columnEnd) {// 升序
            for (int i = columnStart; i <= columnEnd; i++) {
                if (row.getCell(i) != null)
                    list.add(row.getCell(i).toString());
                else
                    list.add("");
            }
        } else {// 降序
            for (int i = columnStart; i >= columnEnd; i--) {
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
    public static int getColumnCount(Sheet sheet, int rownum) {
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
            int columnCount = SheetUtils.getColumnCount(srcSheet, rownum);
            for (int column = 0; column < columnCount; column++) {// column
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
                //  SheetStyleUtils.setColor(desCell, color, borderMode);

                desCell.getCellStyle().setDataFormat(srcCell.getCellStyle().getDataFormat());
            }
        }

        copyColumnWidth(srcSheet, desSheet);
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
            case _NONE:
            case ERROR:
                break;
        }
    }

    /**
     * 设置Excel单元格颜色
     */
    public static void setColor(Cell cell, java.awt.Color color, BorderMode borderMode) {
        if (cell instanceof HSSFCell) {
            HSSFCell myCell = (HSSFCell) cell;
            HSSFWorkbook work = myCell.getSheet().getWorkbook();
            setHssfColor(work.getCustomPalette(), myCell, myCell.getCellStyle(), color, borderMode);
            return;
        }
        if (cell instanceof XSSFCell) {
            XSSFCell myCell = (XSSFCell) cell;
            setXssfColor(myCell, myCell.getCellStyle(), color, borderMode);
            return;
        }
    }

    /**
     * HSSF设置样式
     *
     * @param palette
     * @param cell
     * @param style
     * @param color
     */
    public static void setHssfColor(HSSFPalette palette, HSSFCell cell, HSSFCellStyle style, java.awt.Color color,
                                    BorderMode borderMode) {
        setBorder(borderMode, style);
        if (color != null && color.getRGB() != -16777216) {
            HSSFColor myColor = palette.findSimilarColor(color.getRed(), color.getGreen(), color.getBlue());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(myColor.getIndex());
        }
        cell.setCellStyle(style);
    }

    /**
     * XSSF设置样式
     *
     * @param cell
     * @param style
     * @param color
     */
    public static void setXssfColor(XSSFCell cell, XSSFCellStyle style, java.awt.Color color, BorderMode borderMode) {
        setBorder(borderMode, style);
        if (color != null && color.getRGB() != -16777216) {
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFColor xssfColor = new XSSFColor(color, new DefaultIndexedColorMap());
            style.setFillForegroundColor(xssfColor);
        }
        cell.setCellStyle(style);
    }

    public static void copyColumnWidth(Sheet srcSheet, Sheet desSheet) {
        int size = srcSheet.getRow(0).getLastCellNum();
        for (int i = 0; i < size; i++) {
            desSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
        }
    }

    public static void setBorder(BorderMode borderMode, CellStyle cellStyle) {
        cellStyle.setBorderBottom(borderMode.getBottom());
        cellStyle.setBorderRight(borderMode.getRight());
        cellStyle.setBorderLeft(borderMode.getLeft());
        cellStyle.setBorderTop(borderMode.getTop());
    }

    public static BorderMode getBorderMode(CellStyle style) {
        BorderMode mode = new BorderMode();
        mode.setBottom(style.getBorderBottom());
        mode.setLeft(style.getBorderLeft());
        mode.setRight(style.getBorderRight());
        mode.setTop(style.getBorderTop());

        return mode;
    }

    /**
     * 获取单一列的所有批注
     *
     * @param sheet
     * @param column 批注提取列
     */
    public static HashMap<Integer, String> getSheetRichString(Sheet sheet, int column) {
        int rowCount = SheetUtils.getRowCount(sheet);
        HashMap<Integer, String> richMap = new HashMap<Integer, String>();
        for (int i = 0; i < rowCount; i++) {
            int aa = i;
            Optional<Cell> cellOpt = SheetUtils.getCell(sheet, i, column);
            if (cellOpt.isEmpty())
                continue;
            if (cellOpt.get().getCellComment() == null)
                continue;
            String str = cellOpt.get().getCellComment().getString().getString();
            richMap.put(aa, str.substring(str.indexOf(":") + 1).trim());
        }
        return richMap;
    }

    /**
     * 列染色统计。
     */
    public static List<Integer> ColorRowColumnCount(Sheet sheet, int column) {
        int rowCount = SheetUtils.getRowCount(sheet);
        List<Integer> colourCount = new ArrayList<Integer>();
        for (int i = 0; i < rowCount; i++) {
            Optional<Cell> cellOptional = SheetUtils.getCell(sheet, i, column);
            if (cellOptional.isEmpty()) {
                continue;
            }
            Cell cell = cellOptional.get();
            CellStyle cellStyle = cell.getCellStyle();
            short s = cellStyle.getFillForegroundColor();
            if (s != 64) {
                colourCount.add(i);
            }
        }
        return colourCount;
    }

    /**
     * 统计所有行数值重复行： 将单元格内容相加取唯一行；限定条件在关键列
     *
     * @param sheet
     * @param ignoreRowsInfo 忽略行的信息
     * @return 返回重复行的行号。
     */
    public HashMap<Integer, CellAddress> repeatRow(Sheet sheet, HashMap<Integer, CellAddress> ignoreRowsInfo) {
        return repeatRow(sheet, getAllColumnArray(sheet), ignoreRowsInfo);
    }

    /**
     * 统计所有行数值重复行： 将单元格内容相加取唯一行
     *
     * @param list           定义列
     * @param ignoreRowsInfo 忽略行的信息
     * @return 返回重复行的行号。
     */
    public HashMap<Integer, CellAddress> repeatRow(Sheet sheet, List<Integer> list,
                                                   HashMap<Integer, CellAddress> ignoreRowsInfo) {
        int columnCount = list.size();
        int rowCount = SheetUtils.getRowCount(sheet);
        List<CellRangeAddress> regions = sheet.getMergedRegions();
        HashSet<String> hashSet = new HashSet<String>();
        HashMap<Integer, CellAddress> repeatHashSet = new HashMap<Integer, CellAddress>();
        for (int i = 0; i < rowCount; i++) {
            if (ignoreRowsInfo.get(i) != null) {
                continue;
            }
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < columnCount; j++) {
                String value = getCellValue(sheet, i, list.get(j), regions);
                sb.append(value);
            }
            if (hashSet.contains(sb.toString())) {
                repeatHashSet.put(i, null);
                continue;
            }
            hashSet.add(sb.toString());
        }
        return repeatHashSet;
    }

    /**
     * 获取单元格内值，当单元格为空时，取合并单元格内数值，不存在时，置空=""。
     */
    public static String getCellValue(Sheet sheet, int rownum, int column, List<CellRangeAddress> regions) {
        Optional<Cell> cellOptional = SheetUtils.getCell(sheet, rownum, column);
        if (cellOptional.isEmpty())
            return null;
        Cell cell = cellOptional.get();

        if (cell.getCellType() == CellType.BLANK) {
            CellAddress address = getRangeFirstCell(regions, cell);// 检查单元格是否是合并单元格。
            if (address == null) {
                return "";
            }
            cellOptional = SheetUtils.getCell(sheet, address.getColumn(), address.getRow());
            if (cellOptional.isEmpty())
                return null;
            cell = cellOptional.get();
            return cell.toString();
        }
        return cell.toString();
    }

    /**
     * 获取单元格内值，当单元格为空时，取合并单元格内数值，不存在时，置空=""。
     */
    public static Cell getCell(Sheet sheet, int rownum, int column, List<CellRangeAddress> regions) {
        Optional<Cell> co = Optional.ofNullable(sheet).map(s -> s.getRow(rownum)).map(r -> r.getCell(column));
        if (co.isEmpty())
            return null;
        Cell cell = co.get();
        if (cell.getCellType() == CellType.BLANK) {
            CellAddress address = getRangeFirstCell(regions, cell);// 检查单元格是否是合并单元格。
            if (address == null)
                return null;
            Optional<Cell> cnNew = Optional.ofNullable(sheet).map(s -> s.getRow(address.getRow()))
                    .map(r -> r.getCell(address.getColumn()));
            if (cnNew.isEmpty())
                return null;
            return cnNew.get();
        }
        return cell;
    }

    /**
     * 统计表格所有列 有效行数
     */
    public static HashMap<Integer, CellAddress> EffectiveRows(Sheet sheet) {
        return EffectiveRows(sheet, getAllColumnArray(sheet));
    }

    /**
     * 获取所有列
     */
    private static ArrayList<Integer> getAllColumnArray(Sheet sheet) {
        int columnCount = getSheetMaxColumn(sheet);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < columnCount; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * 有效行数统计，自定义列号，有空时，为无效行。
     *
     * @param sheet
     * @param list
     */
    public static HashMap<Integer, CellAddress> EffectiveRows(Sheet sheet, List<Integer> list) {
        int rowCount = getRowCount(sheet);
        int columnCount = list.size();
        HashMap<Integer, CellAddress> hashSet = new HashMap<Integer, CellAddress>();
        List<CellRangeAddress> regions = sheet.getMergedRegions();
        int i, j = 0;
        for (i = 0; i < rowCount; i++) {
            boolean a = true;
            j = 0;
            for (j = 0; j < columnCount; j++) {
                String str = getCellValue(sheet, i, list.get(j), regions);
                if (str == null || str.equals("")) {
                    a = false;
                    break;
                }
                if (j == columnCount - 1)
                    break;
            }
            if (!a) {// 无效行统计
                hashSet.put(i, new CellAddress(i, list.get(j)));
            }
        }
        return hashSet;
    }

    /**
     * 验证cell是否在合并单元格内，存在返回对应单元格地址，不存在返回NULL
     */
    public static CellAddress getRangeFirstCell(List<CellRangeAddress> regions, Cell cell) {
        for (CellRangeAddress cellRangeAddress : regions) {
            if (cellRangeAddress.isInRange(cell)) {
                return new CellAddress(cellRangeAddress.getFirstRow(), cellRangeAddress.getFirstColumn());
            }
        }
        return null;
    }

    /**
     * 获取sheet 最大列数
     */
    public static int getSheetMaxColumn(Sheet sheet) {
        int row = getRowCount(sheet);
        int maxColumn = 0;
        for (int i = 0; i < row; i++) {
            int newColumn = SheetUtils.getColumnCount(sheet, i);
            if (maxColumn < newColumn)
                maxColumn = newColumn;
        }
        return maxColumn;
    }

}
