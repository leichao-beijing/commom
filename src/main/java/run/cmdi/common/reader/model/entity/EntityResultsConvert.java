package run.cmdi.common.reader.model.entity;

import lombok.Getter;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.reader.core.FieldInfos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 多行数据
 *
 * @author leichao
 */
@Getter
public class EntityResultsConvert<T> {
    public EntityResultsConvert(int readHeadRownum, int sheetMaxRow, FieldInfos filedInfos) {
        this.readHeadRownum = readHeadRownum;
        this.sheetMaxRow = sheetMaxRow;
        this.filedInfos = filedInfos;
    }

    private Set<String> errorType = new HashSet<>();

    /**
     * 读取头的起始行数
     */
    private final int readHeadRownum;
    /**
     * 匹配对应的sheet
     */
    private final FieldInfos filedInfos;
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
    private final List<LocationTag<T>> resultList = new ArrayList<LocationTag<T>>();

    public void upDateErrorType() {
        if (cellErrorList.size() != 0) {
            for (CellAddressAndMessage message : cellErrorList) {
                errorType.add(message.getEx().getTypeName());
            }
        }
    }

    public void addResult(EntityResultConvert<T> result, boolean state) {
        if (result.getCellErrorList() != null) {
            result.upDateErrorType();
            cellErrorList.addAll(result.getCellErrorList());
        }
        if (result.getErrorType() != null)
            errorType.addAll(result.getErrorType());
        if (!state || result.getCellErrorList().isEmpty())
            resultList.add(result.getResult());
    }
}
