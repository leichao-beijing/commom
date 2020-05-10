package run.cmid.common.excel.model.entity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import lombok.Getter;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.excel.model.SheetModel;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;
import run.cmid.common.excel.plugins.SheetStyle;
import run.cmid.common.excel.plugins.SheetUtils;

/**
 * 
 * @author leichao
 */
@Getter
public class ExcelListResult<T> {
    public ExcelListResult(int readHeadRownum, int sheetMaxRow, Sheet sheet, SheetModel<T> mode) {
        this.readHeadRownum = readHeadRownum;
        this.sheetMaxRow = sheetMaxRow;
        this.sheet = sheet;
        this.mode = mode;
    }

    private Set<ExcelExceptionType> errorType = EnumSet.noneOf(ExcelExceptionType.class);

    private final SheetModel<T> mode;
    /**
     * 读取头的起始行数
     */
    private final int readHeadRownum;
    /**
     * 匹配对应的sheet
     */
    private final Sheet sheet;
    /***
     * sheet最大有效行数
     */
    private final int sheetMaxRow;
    /**
     * check Error CellAddress Map
     */
    private final List<CellAddressAndMessage> cellErrorList = new ArrayList<CellAddressAndMessage>();

    /**
     * 返回结果List
     */
    private final List<LocationTag<T>> rusultList = new ArrayList<LocationTag<T>>();

    public void upDateErrorType() {
        if (cellErrorList.size() != 0) {
            for (CellAddressAndMessage message : cellErrorList) {
                errorType.add(message.getEx());
            }
        }
    }

    public void addResult(ExcelResult<T> result) {
        if (result.getCellRowErrorList() != null && result.getCellRowErrorList().size() != 0)
            cellErrorList.addAll(result.getCellRowErrorList());
        if (result.getResult() != null)
            rusultList.add(result.getResult());
    }

    /**
     * 将问题内容对应单元格fill红背景
     */
    public void writeErrorList() {
        writeErrorListMessage(cellErrorList);
    }

    /**
     * 将文件问题数据写入文件 读取位置进行单元格标红
     */
    public void writeErrorList(List<CellAddress> list) {
        for (CellAddress cellAddress : list) {
            fillCell(cellAddress);
        }
    }

    /**
     * 将文件问题数据写入文件 读取位置进行单元格标红
     */
    public void writeErrorListMessage(List<CellAddressAndMessage> list) {
        for (CellAddress cellAddress : list) {
            fillCell(cellAddress);
        }
    }

    private void fillCell(CellAddress cellAddress) {
        SheetStyle sheetStyle = new SheetStyle(sheet);
        Cell cell = SheetUtils.getCreateCell(sheet, cellAddress.getRow(), cellAddress.getColumn());
        cell.setCellStyle(sheetStyle.redBorder(cell.getCellStyle()));
    }
}
