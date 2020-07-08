package run.cmid.common.poi.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import run.cmid.common.io.StringUtils;
import run.cmid.common.poi.model.StyleInfo;

public class StylePalette {
    private final FormatPalette formatPalette;
    private final Workbook workbook;
    private Map<StyleInfo, CellStyle> map = new HashMap<>();

    public CellStyle createStyle(StyleInfo styleInfo) {
        CellStyle cellStyle = map.get(styleInfo);
        if (cellStyle != null)
            return cellStyle;
        cellStyle = style(styleInfo);
        map.put(styleInfo, cellStyle);
        return cellStyle;
    }

    public StylePalette(Workbook workbook) {
        this.workbook = workbook;
        this.formatPalette = new FormatPalette(workbook);
    }

    private CellStyle style(StyleInfo styleInfo) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(styleInfo.getBorderTop());
        cellStyle.setBorderBottom(styleInfo.getBorderBottom());
        cellStyle.setBorderLeft(styleInfo.getBorderLeft());
        cellStyle.setBorderRight(styleInfo.getBorderRight());
        if (styleInfo.getTopBorderColor() != null)
            cellStyle.setTopBorderColor(styleInfo.getTopBorderColor());
        if (styleInfo.getLeftBorderColor() != null)
            cellStyle.setLeftBorderColor(styleInfo.getLeftBorderColor());
        if (styleInfo.getBottomBorderColor() != null)
            cellStyle.setBottomBorderColor(styleInfo.getBottomBorderColor());
        if (styleInfo.getRightBorderColor() != null)
            cellStyle.setRightBorderColor(styleInfo.getRightBorderColor());

        if (styleInfo.getFillBackgroundColor() != null) {
            copyColorBackgroundColor(styleInfo.getFillBackgroundColor(), workbook, cellStyle);
        }
        if (styleInfo.getFillForegroundColor() != null) {
            copyColorForegroundColor(styleInfo.getFillForegroundColor(), workbook, cellStyle);
        }
        cellStyle.setFillPattern(styleInfo.getFillPattern());
        cellStyle.setHidden(styleInfo.isHidden());
        if (styleInfo.getFormat() != null)
            cellStyle.setDataFormat(formatPalette.getFormatIndex(styleInfo.getFormat()));
        cellStyle.setWrapText(styleInfo.isWrapText());
        org.apache.poi.ss.usermodel.Color sss = cellStyle.getFillForegroundColorColor();
        return cellStyle;
    }

    public static void copyColorBackgroundColor(Color srcColor, Workbook desWorkbook, CellStyle desStyle) {
        if (desWorkbook instanceof HSSFWorkbook && desStyle instanceof HSSFCellStyle) {
            short i = color((HSSFWorkbook) desWorkbook, srcColor);
            desStyle.setFillBackgroundColor(i);
            return;
        }
        if (desWorkbook instanceof XSSFWorkbook && desStyle instanceof XSSFCellStyle) {
            XSSFColor color = color((XSSFWorkbook) desWorkbook, srcColor);
            ((XSSFCellStyle) desStyle).setFillBackgroundColor(color);
            return;
        }
        throw new NullPointerException("XSSFWorkbook or XSSFCellStyle HSSFWorkbook and HSSFCellStyle");
    }

    public static void copyColorForegroundColor(Color srcColor, Workbook desWorkbook, CellStyle desStyle) {
        if (srcColor == null)
            return;
        if (desWorkbook instanceof HSSFWorkbook && desStyle instanceof HSSFCellStyle) {
            short i = color((HSSFWorkbook) desWorkbook, srcColor);
            desStyle.setFillForegroundColor(i);
            return;
        }
        if (desWorkbook instanceof XSSFWorkbook && desStyle instanceof XSSFCellStyle) {
            XSSFColor color = color((XSSFWorkbook) desWorkbook, srcColor);
            ((XSSFCellStyle) desStyle).setFillForegroundColor(color);
            return;
        }
        throw new NullPointerException("XSSFWorkbook or XSSFCellStyle HSSFWorkbook and HSSFCellStyle");
    }

    private static short color(HSSFWorkbook workbook, Color color) {
        return workbook.getCustomPalette()
                .findSimilarColor((byte) color.getRed(), (byte) color.getRGB(), (byte) color.getBlue()).getIndex();
    }

    private static XSSFColor color(XSSFWorkbook workbook, Color color) {
        return new XSSFColor(color, new DefaultIndexedColorMap());
    }

    public static java.awt.Color getColor(CellStyle style) {
        org.apache.poi.ss.usermodel.Color color = style.getFillForegroundColorColor();
        return getColor(color);
    }

    public static Color getColor(org.apache.poi.ss.usermodel.Color color) {
        if (color == null)
            return null;
        if (color instanceof HSSFColor) {
            HSSFColor myColor = (HSSFColor) color;
            short[] tr = myColor.getTriplet();
            String string = myColor.getHexString();
            if (tr[0] == 0 && tr[1] == 0 && tr[2] == 0)
                return null;
            return new Color(tr[0], tr[1], tr[2]);
        }
        if (color instanceof XSSFColor) {
            XSSFColor myColor = (XSSFColor) color;
            byte[] bb = myColor.getRGBWithTint();
            if (bb == null)
                return null;
            return new Color(StringUtils.unsignedInt(bb[0]), StringUtils.unsignedInt(bb[1]),
                    StringUtils.unsignedInt(bb[2]));
        }
        return null;
    }
}
