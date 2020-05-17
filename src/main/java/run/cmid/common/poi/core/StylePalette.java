package run.cmid.common.poi.core;

import cn.hutool.core.collection.CollUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import run.cmid.common.io.StringUtils;
import run.cmid.common.poi.model.StyleInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
            cellStyle.setTopBorderColor(getColorIndex(styleInfo.getTopBorderColor()));
        if (styleInfo.getLeftBorderColor() != null)
            cellStyle.setLeftBorderColor(getColorIndex(styleInfo.getLeftBorderColor()));
        if (styleInfo.getBottomBorderColor() != null)
            cellStyle.setBottomBorderColor(getColorIndex(styleInfo.getBottomBorderColor()));
        if (styleInfo.getRightBorderColor() != null)
            cellStyle.setRightBorderColor(getColorIndex(styleInfo.getRightBorderColor()));
        if (styleInfo.getFillBackgroundColor() != null)
            cellStyle.setFillBackgroundColor(getColorIndex(styleInfo.getFillBackgroundColor()));
        if (styleInfo.getFillForegroundColor() != null)
            cellStyle.setFillForegroundColor(getColorIndex(styleInfo.getFillForegroundColor()));
        cellStyle.setFillPattern(styleInfo.getFillPattern());
        cellStyle.setHidden(styleInfo.isHidden());
        if (styleInfo.getFormat() != null)
            cellStyle.setDataFormat(formatPalette.getFormatIndex(styleInfo.getFormat()));
        cellStyle.setWrapText(styleInfo.isWrapText());
        return cellStyle;
    }

    public short getColorIndex(Color color) {
        if (workbook instanceof HSSFWorkbook) {
            return color((HSSFWorkbook) workbook, color);
        }
        if (workbook instanceof XSSFWorkbook) {
            return color((XSSFWorkbook) workbook, color);
        }
        throw new NullPointerException("ColorPalette.getColorIndex");
    }

    private short color(HSSFWorkbook workbook, Color color) {
        return workbook.getCustomPalette().findSimilarColor((byte) color.getRed(), (byte) color.getRGB(), (byte) color.getBlue()).getIndex();
    }

    private short color(XSSFWorkbook workbook, Color color) {
        return new XSSFColor(color, new DefaultIndexedColorMap()).getIndex();
    }

    public static java.awt.Color getColor(CellStyle style) {
        org.apache.poi.ss.usermodel.Color color = style.getFillForegroundColorColor();
        return getColor(color);
    }

    static Color getColor(org.apache.poi.ss.usermodel.Color color) {
        if (color == null)
            return null;
        if (color instanceof HSSFColor) {
            HSSFColor myColor = (HSSFColor) color;
            short[] tr = myColor.getTriplet();
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
