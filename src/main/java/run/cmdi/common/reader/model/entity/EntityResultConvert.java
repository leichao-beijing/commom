package run.cmdi.common.reader.model.entity;

import lombok.Getter;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.reader.core.ReaderPage;
import run.cmdi.common.reader.model.HeadInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 当行数据
 *
 * @author leichao
 */
@Getter
public class EntityResultConvert<T> {
    public EntityResultConvert(int rownum, LocationTag<T> result, Map<Integer, CellAddressAndMessage> cellErrorList) {
        this.rownum = rownum;
        this.result = result;
        this.cellErrorList = cellErrorList;
    }

    private Set<String> errorType = new HashSet<>();

    /***
     * 读取的当前行号
     */
    private final int rownum;
    /**
     * check Error CellAddress Map
     */
    private final Map<Integer, CellAddressAndMessage> cellErrorList;

    /**
     * 返回结果List
     */
    @Getter
    private final LocationTag<T> result;


    public void upDateErrorType() {
        if (cellErrorList.size() != 0) {
            cellErrorList.forEach((v,k)->errorType.addAll(k.getSetEx()));
//            for (CellAddressAndMessage message : cellErrorList) {
//                errorType.add(message.getEx().getTypeName());
//            }
        }
    }
}
