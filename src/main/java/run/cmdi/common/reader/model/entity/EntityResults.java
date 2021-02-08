package run.cmdi.common.reader.model.entity;

import java.util.*;

import lombok.Getter;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.reader.core.ReaderPage;
import run.cmdi.common.reader.model.HeadInfo;

/**
 * 多行数据
 *
 * @author leichao
 */
@Getter
public class EntityResults<T, PAGE, UNIT> {
    public EntityResults(int readHeadRownum, int sheetMaxRow, ReaderPage readerPage, HeadInfo<PAGE, UNIT> mode) {
        this.readHeadRownum = readHeadRownum;
        this.sheetMaxRow = sheetMaxRow;
        this.readerPage = readerPage;
        this.mode = mode;
    }

    private Set<String> errorType = new HashSet<>();

    private final HeadInfo<PAGE, UNIT> mode;
    /**
     * 读取头的起始行数
     */
    private final int readHeadRownum;
    /**
     * 匹配对应的sheet
     */
    private final ReaderPage<PAGE, UNIT> readerPage;
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
//        if (cellErrorList.size() != 0) {
//            for (CellAddressAndMessage message : cellErrorList) {
//                errorType.add(message.getEx().getTypeName());
//            }
//        }
    }

    public void addResult(EntityResult<T, PAGE, UNIT> result, boolean state) {
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
