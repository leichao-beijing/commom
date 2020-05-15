package run.cmid.common.excel.plugins;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leichao
 * @date 2020-04-28 10:49:00
 */
@Getter
public class StyleInfo {
    private Map<String,Object> data=new  HashMap<String,Object>() ;
    private BorderStyle borderBottom=BorderStyle.NONE;
    private BorderStyle borderLeft=BorderStyle.NONE;
    private BorderStyle borderRight=BorderStyle.NONE;
    private BorderStyle borderTop=BorderStyle.NONE;
    private short dataFormat;
    private short fillForegroundColor;
    private short fillBackgroundColor;
    private short fillBackgroundColorColorIndex;
    private short bottomBorXderColor;
    private boolean hidden;
    private short leftBorderColor;
    private short bottomBorderColor;
    private short rightBorderColor;
    private short topBorderColor;
    private boolean wrapText;

    public StyleInfo() {

    }

    @Override
    public int hashCode() {
        return data.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Map){
            Map map= (Map) obj;
            return  data.toString().equals(map.toString());
        }
        return super.equals(obj);
    }



    public void setBorderBottom(BorderStyle borderBottom) {
        data.put("borderBottom",borderBottom.getCode());
        this.borderBottom = borderBottom;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        data.put("borderLeft",borderLeft.getCode());
        this.borderLeft = borderLeft;
    }

    public void setBorderRight(BorderStyle borderRight) {
        data.put("borderRight",borderRight.getCode());
        this.borderRight = borderRight;
    }

    public void setBorderTop(BorderStyle borderTop) {
        data.put("borderTop",borderTop.getCode());

        this.borderTop = borderTop;
    }

    public void setDataFormat(short dataFormat) {
        data.put("dataFormat",dataFormat);
        this.dataFormat = dataFormat;
    }

    public void setFillForegroundColor(short fillForegroundColor) {
        data.put("fillForegroundColor",fillForegroundColor);
        this.fillForegroundColor = fillForegroundColor;
    }

    public void setFillBackgroundColor(short fillBackgroundColor) {
        data.put("fillBackgroundColor",fillBackgroundColor);
        this.fillBackgroundColor = fillBackgroundColor;
    }

    public void setFillBackgroundColorColorIndex(short fillBackgroundColorColorIndex) {
        data.put("fillBackgroundColorColorIndex",fillBackgroundColorColorIndex);
        this.fillBackgroundColorColorIndex = fillBackgroundColorColorIndex;
    }

    public void setBottomBorXderColor(short bottomBorXderColor) {
        data.put("bottomBorXderColor",bottomBorXderColor);
        this.bottomBorXderColor = bottomBorXderColor;
    }

    public void setHidden(boolean hidden) {
        data.put("hidden",hidden);
        this.hidden = hidden;
    }

    public void setLeftBorderColor(short leftBorderColor) {
        data.put("leftBorderColor",leftBorderColor);
        this.leftBorderColor = leftBorderColor;
    }

    public void setBottomBorderColor(short bottomBorderColor) {
        data.put("bottomBorderColor",bottomBorderColor);
        this.bottomBorderColor = bottomBorderColor;
    }

    public void setRightBorderColor(short rightBorderColor) {
        data.put("rightBorderColor",rightBorderColor);
        this.rightBorderColor = rightBorderColor;
    }

    public void setTopBorderColor(short topBorderColor) {
        data.put("topBorderColor",topBorderColor);
        this.topBorderColor = topBorderColor;
    }

    public void setWrapText(boolean wrapText) {
        data.put("wrapText",wrapText);
        this.wrapText = wrapText;
    }
}
