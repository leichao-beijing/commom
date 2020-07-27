package run.cmdi.common.poi.model;

import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import run.cmdi.common.poi.core.StylePalette;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leichao
 * @date 2020-04-28 10:49:00
 */
@Getter
public class StyleInfo {


    public StyleInfo() {
    }

    public StyleInfo(CellStyle cellStyle) {
        setFillBackgroundColor(StylePalette.getColor(cellStyle.getFillBackgroundColorColor()));
        setFillForegroundColor(StylePalette.getColor(cellStyle.getFillForegroundColorColor()));

        setLeftBorderColor(cellStyle.getLeftBorderColor());
        setBottomBorderColor(cellStyle.getBottomBorderColor());
        setRightBorderColor(cellStyle.getRightBorderColor());
        setTopBorderColor(cellStyle.getTopBorderColor());

        setFillPattern(cellStyle.getFillPattern());

        setWrapText(cellStyle.getWrapText());
        setHidden(cellStyle.getHidden());

        setFormat(cellStyle.getDataFormatString());


        setBorderBottom(cellStyle.getBorderBottom());
        setBorderLeft(cellStyle.getBorderLeft());
        setBorderRight(cellStyle.getBorderRight());
        setBorderTop(cellStyle.getBorderTop());
    }

    private Map<String, Object> data = new HashMap<String, Object>();
    private BorderStyle borderBottom = BorderStyle.NONE;
    private BorderStyle borderLeft = BorderStyle.NONE;
    private BorderStyle borderRight = BorderStyle.NONE;
    private BorderStyle borderTop = BorderStyle.NONE;

    private FillPatternType FillPattern = FillPatternType.NO_FILL;
    private String format;
    private Color fillForegroundColor;
    private Color fillBackgroundColor;
    private boolean hidden = false;

    private Short leftBorderColor;
    private Short bottomBorderColor;
    private Short rightBorderColor;
    private Short topBorderColor;
    private boolean wrapText;


    @Override
    public int hashCode() {
        return data.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return data.toString().equals(map.toString());
        }
        return super.equals(obj);
    }


    public void setBorderBottom(BorderStyle borderBottom) {
        data.put("borderBottom", borderBottom.getCode());
        this.borderBottom = borderBottom;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        data.put("borderLeft", borderLeft.getCode());
        this.borderLeft = borderLeft;
    }

    public void setBorderRight(BorderStyle borderRight) {
        data.put("borderRight", borderRight.getCode());
        this.borderRight = borderRight;
    }

    public void setBorderTop(BorderStyle borderTop) {
        data.put("borderTop", borderTop.getCode());
        this.borderTop = borderTop;
    }

    public void setFormat(String format) {
        data.put("format", format);
        this.format = format;
    }

    public void setFillForegroundColor(Color color) {
        data.put("fillForegroundColor", color);
        this.fillForegroundColor = color;
    }

    public void setFillBackgroundColor(Color color) {
        data.put("fillBackgroundColor", color);
        this.fillBackgroundColor = color;
    }

    public void setHidden(boolean hidden) {
        data.put("hidden", hidden);
        this.hidden = hidden;
    }

    public void setLeftBorderColor(short index) {
        data.put("leftBorderColor", index);
        this.leftBorderColor = index;
    }

    public void setBottomBorderColor(short index) {
        data.put("bottomBorderColor", index);
        this.bottomBorderColor = index;
    }

    public void setRightBorderColor(short index) {
        data.put("rightBorderColor", index);
        this.rightBorderColor = index;
    }

    public void setTopBorderColor(short index) {
        data.put("topBorderColor", index);
        this.topBorderColor = index;
    }

    public void setWrapText(boolean wrapText) {
        data.put("wrapText", wrapText);
        this.wrapText = wrapText;
    }

    public void setFillPattern(FillPatternType fillPattern) {
        data.put("fillPattern", fillPattern.name());
        FillPattern = fillPattern;
    }


}
