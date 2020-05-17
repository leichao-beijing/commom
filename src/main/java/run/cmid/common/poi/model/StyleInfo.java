package run.cmid.common.poi.model;

import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leichao
 * @date 2020-04-28 10:49:00
 */
@Getter
public class StyleInfo {
    private Map<String, Object> data = new HashMap<String, Object>();
    private BorderStyle borderBottom = BorderStyle.NONE;
    private BorderStyle borderLeft = BorderStyle.NONE;
    private BorderStyle borderRight = BorderStyle.NONE;
    private BorderStyle borderTop = BorderStyle.NONE;

    private FillPatternType FillPattern = FillPatternType.NO_FILL;
    private String format;
    private Color fillForegroundColor;
    private Color fillBackgroundColor;
    private boolean hidden=false;

    private Color leftBorderColor;
    private Color bottomBorderColor;
    private Color rightBorderColor;
    private Color topBorderColor;
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

    public void setLeftBorderColor(Color color) {
        data.put("leftBorderColor", color);
        this.leftBorderColor = color;
    }

    public void setBottomBorderColor(Color color) {
        data.put("bottomBorderColor", color);
        this.bottomBorderColor = color;
    }

    public void setRightBorderColor(Color color) {
        data.put("rightBorderColor", color);
        this.rightBorderColor = color;
    }

    public void setTopBorderColor(Color color) {
        data.put("topBorderColor", color);
        this.topBorderColor = color;
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
