package run.cmdi.common.reader.model.entity;

import lombok.Getter;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.io.TypeName;
import run.cmdi.common.reader.model.FindFieldInfos;
import run.cmdi.common.utils.MapUtils;
import run.cmdi.common.validator.eumns.Value;

import java.util.*;

/**
 * 多行数据
 *
 * @author leichao
 */
@Getter
public class EntityResultsConvert<T> {
    public EntityResultsConvert(int readHeadRownum, int sheetMaxRow, FindFieldInfos filedInfos) {
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
    private final FindFieldInfos filedInfos;
    /***
     * sheet最大有效行数
     */
    private final int sheetMaxRow;
    /**
     * check Error CellAddress Map
     */
    private final Map<Integer, Map<Integer, CellAddressAndMessage>> tableErrorMap = new HashMap<>();

    public void addError(int row, int column, TypeName ex, String messages) {
        MapUtils.lineMap(tableErrorMap, row, (value) -> {
            if (value == null)
                value = new HashMap<>();
            MapUtils.lineMap(value, column, (message) -> {
                if (message == null)
                    return new CellAddressAndMessage(row, column, ex, messages);
                message.add(ex, messages);
                return message;
            });
            return value;
        });
    }

    private final List<CellAddressAndMessage> heardErrorList = new ArrayList<>();
    /**
     * 返回结果List
     */
    private final List<LocationTag<T>> resultList = new ArrayList<LocationTag<T>>();

    public void upDateErrorType() {
//        if (tableErrorMap.size() != 0) {
//            for (CellAddressAndMessage message : cellErrorMap) {
//                errorType.add(message.getEx().getTypeName());
//            }
//        }
    }

    public void addResult(EntityResultConvert<T> result, boolean state) {
        if (result.getCellErrorList() != null) {
            result.upDateErrorType();
            if (!result.getCellErrorList().isEmpty())
                tableErrorMap.put(result.getRownum(), result.getCellErrorList());
        }
        if (result.getErrorType() != null)
            errorType.addAll(result.getErrorType());
        if (!state || result.getCellErrorList().isEmpty())
            resultList.add(result.getResult());
    }
}
