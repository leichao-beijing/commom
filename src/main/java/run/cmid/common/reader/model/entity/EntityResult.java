package run.cmid.common.reader.model.entity;

import lombok.Getter;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.reader.core.ReaderPage;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 当行数据
 *
 * @author leichao
 */
@Getter
public class EntityResult<T, PAGE, UNIT> {
    public EntityResult(int readHeadRownum, int rownum, HeadInfo<PAGE, UNIT> mode, LocationTag<T> result, List<CellAddressAndMessage> cellErrorList) {
        this.readHeadRownum = readHeadRownum;
        this.rownum = rownum;
        this.readerPage = mode.getReaderPage();
        this.mode = mode;
        this.result = result;
        this.cellErrorList = cellErrorList;
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
     * 读取的当前行号
     */
    private final int rownum;
    /**
     * check Error CellAddress Map
     */
    private final List<CellAddressAndMessage> cellErrorList;

    /**
     * 返回结果List
     */
    @Getter
    private final LocationTag<T> result;


    public void upDateErrorType() {
        if (cellErrorList.size() != 0) {
            for (CellAddressAndMessage message : cellErrorList) {
                errorType.add(message.getEx().getTypeName());
            }
        }
    }
}
