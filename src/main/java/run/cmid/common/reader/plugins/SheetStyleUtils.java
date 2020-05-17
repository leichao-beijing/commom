package run.cmid.common.reader.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import run.cmid.common.reader.model.to.BorderMode;
import run.cmid.common.io.StringUtils;

/**
 * 获取Sheet内容工具类。<br>
 * 
 */
public class SheetStyleUtils {

    public static java.awt.Color getColor(CellStyle style) {
        Color color = style.getFillForegroundColorColor();
        if (color == null)
            return null;
        if (color instanceof HSSFColor) {
            HSSFColor myColor = (HSSFColor) color;
            short[] tr = myColor.getTriplet();
            return new java.awt.Color(tr[0], tr[1], tr[2]);
        }
        if (color instanceof XSSFColor) {
            XSSFColor myColor = (XSSFColor) color;
            byte[] bb = myColor.getRGBWithTint();
            if (bb == null)
                return null;
            return new java.awt.Color(StringUtils.unsignedInt(bb[0]), StringUtils.unsignedInt(bb[1]),
                    StringUtils.unsignedInt(bb[2]));
        }
        return null;
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
    public static List<Integer> staticgetColourRowCount(Sheet sheet, int column) {
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

    public static String getCellRowList(Sheet sheet, int rownum, int column, List<CellRangeAddress> regions) {
        Optional<Cell> cellOptional = SheetUtils.getCell(sheet, rownum, column);
        if (cellOptional.isEmpty())
            return null;
        Cell cell = cellOptional.get();
        if (cell.getCellType() != CellType.BLANK)
            return cell.toString();
        CellAddress address = getReginsFirstCell(regions, cell);// 检查单元格是否是合并单元格。
        if (address == null)
            return null;
        cellOptional = SheetUtils.getCell(sheet, address.getRow(), address.getColumn());
        if (cellOptional.isEmpty())
            return null;
        return cell.toString();
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
            CellAddress address = getReginsFirstCell(regions, cell);// 检查单元格是否是合并单元格。
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
            CellAddress address = getReginsFirstCell(regions, cell);// 检查单元格是否是合并单元格。
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
        int rowCount = SheetUtils.getRowCount(sheet);
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
    public static CellAddress getReginsFirstCell(List<CellRangeAddress> regions, Cell cell) {
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
        int row = SheetUtils.getRowCount(sheet);
        int maxColumn = 0;
        for (int i = 0; i < row; i++) {
            int newColumn = SheetUtils.getColumnCount(sheet, i);
            if (maxColumn < newColumn)
                maxColumn = newColumn;
        }
        return maxColumn;
    }
}
