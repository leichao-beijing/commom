package run.cmid.common.excel.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.compare.model.LocationTagError;
import run.cmid.common.excel.annotations.ExcelConverterHead;
import run.cmid.common.excel.annotations.Index;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.SheetModel;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;
import run.cmid.common.excel.model.to.ExcelHeadModel;
import run.cmid.common.excel.plugins.SheetUtils;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * 通过对象class检索Excel内对应sheet
 * 
 * @author leichao
 */
@Getter
public class ExcelBuildings<T> {

    @Getter
    private final Class<T> clazz;
    @Getter
    private final List<FieldDetail<T>> list;
    @Getter
    private final ExcelHeadModel headModel;
    @Getter
    private final List<List<String>> indexes;
    @Getter
    @Setter
    private String sheetName;

    /**
     * @param clazz
     */
    public ExcelBuildings(Class<T> clazz) {
        this.clazz = clazz;
        ExcelConverterHead head = clazz.getAnnotation(ExcelConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ExcelConverterHead not enable");
        isIndexMethod(head.indexes(), clazz);// 验证idnex内值是否存在于对象中。
        indexes = getIndexList(head.indexes());
        headModel = new ExcelHeadModel(head);
        sheetName = head.sheetName();
        List<String> values = new ArrayList<String>();
        for (List<String> index : indexes) {
            values.addAll(index);
        }
        list = ConverterFieldDetail.toList(clazz, headModel, values);
    }

    /**
     * 默认从第0行读取头，不进行合并单元格计算。
     * 
     * @param workbook
     * @exception ConverterExcelException
     */
    public ExcelBuild<T> find(Workbook workbook) throws ConverterExcelException {
        return find(workbook, 0, false, sheetName);
    }

    /**
     * Excel文件进行匹配，默认不进行合并单元格计算。
     * 
     * @param workbook
     * @param readHeadRownum 头读取行
     * @exception ConverterExcelException
     */
    public ExcelBuild<T> find(Workbook workbook, int readHeadRownum) throws ConverterExcelException {
        return find(workbook, readHeadRownum, false, sheetName);
    }

    /**
     * Excel文件进行匹配。
     * 
     * @param workbook
     * @param readHeadRownum 头读取行
     * @param rangeState     单元格合并计算
     * @param sheetName      等于""时，忽略。反之直接通过sheetName读取sheet
     * @exception ConverterExcelException
     */
    public ExcelBuild<T> find(Workbook workbook, int readHeadRownum, boolean rangeState, String sheetName)
            throws ConverterExcelException {

        SheetModel<T> mode = new FindWorkbookMatchSheet<T>(list, headModel, readHeadRownum).find(workbook, sheetName);// 查找sheet
        int sheetMaxRow = SheetUtils.sheetCount(mode.getSheet(), mode.getResponse().getList());// 获取最大行
        Map<ExcelExceptionType, String> errorType = computeErrorType(mode.getResponse().getErrorList());
        if (errorType.size() != 0)
            throw new ConverterExcelException(errorType);
        HashMap<CellAddress, CellAddress> cellRangeMap = null;
        if (rangeState)
            cellRangeMap = SheetUtils.computeRangeCellMap(mode.getSheet());
        else
            cellRangeMap = null;
        return new ExcelBuildWorkBook<T>(sheetMaxRow, clazz, mode, cellRangeMap, indexes, readHeadRownum);
    }

    private Map<ExcelExceptionType, String> computeErrorType(
            List<LocationTagError<FieldDetail<T>, ConverterExcelException>> columnErrorList) {
        EnumMap<ExcelExceptionType, String> errorTypeMap = new EnumMap<ExcelExceptionType, String>(
                ExcelExceptionType.class);
        for (LocationTagError<FieldDetail<T>, ConverterExcelException> locationTagError : columnErrorList) {
            if (errorTypeMap.get(locationTagError.getEx().getType()) == null) {
                errorTypeMap.put(locationTagError.getEx().getType(), locationTagError.getEx().getMessage());
                continue;
            }
            errorTypeMap.put(locationTagError.getEx().getType(), errorTypeMap.get(locationTagError.getEx().getType())
                    + " " + locationTagError.getEx().getMessageValue());
        }
        return errorTypeMap;
    }

    protected static <T> void isIndexMethod(Index[] indexes, Class<T> clazz) {
        HashSet<String> set = new HashSet<String>();
        for (Index index : indexes) {
            String[] values = index.value();
            for (String value : values) {
                set.add(value);
            }
        }
        set.forEach((value) -> {
            if (ReflectUtil.getMethodByName(clazz, false, ReflectLcUtils.methodGetString(value)) == null)
                throw new NullPointerException("index;" + value + " Set method no find.");
//            if (ReflectUtil.getMethodByName(clazz, false, ReflectLcUtils.methodGetString(value)) == null)
//                throw new NullPointerException("index;" + value + " Get method no find.");
        });
    }

    protected static <T> List<List<String>> getIndexList(Index[] indexs) {
        List<List<String>> lists = new ArrayList<>();
        for (Index index : indexs) {
            String[] values = index.value();
            List<String> list = new ArrayList<>();
            for (String value : values) {
                list.add(value);// "get" + ReflectParameter.upperCase(value));
            }
            if (list.size() != 0)
                lists.add(list);
            else
                list.clear();
        }
        return lists;
    }
}
