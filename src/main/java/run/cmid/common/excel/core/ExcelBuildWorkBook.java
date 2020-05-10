package run.cmid.common.excel.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.SheetUtil;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.compare.model.LocationTagError;
import run.cmid.common.compare.model.QepeatResponse;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.SheetModel;
import run.cmid.common.excel.model.entity.CellAddressAndMessage;
import run.cmid.common.excel.model.entity.ExcelConverterEntity;
import run.cmid.common.excel.model.entity.ExcelListResult;
import run.cmid.common.excel.model.entity.ExcelResult;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.io.EnumUtil;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * 
 * @author leichao
 */
public class ExcelBuildWorkBook<T> implements ExcelBuild<T> {

    private int sheetMaxRow;
    private final Class<T> classes;
    private final SheetModel<T> mode;
    private final HashMap<CellAddress, CellAddress> cellRangeMap;
    private final List<LocationTagError<FieldDetail<T>, ConverterExcelException>> columnErrorList;
    private final List<List<String>> indexes;
    private final int readHeadRownum;
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public ExcelBuildWorkBook(int sheetMaxRow, Class<T> classes, SheetModel<T> mode,
                              HashMap<CellAddress, CellAddress> cellRangeMap, List<List<String>> indexes, int readHeadRownum) {
        this.sheetMaxRow = sheetMaxRow;
        this.classes = classes;
        this.mode = mode;
        this.cellRangeMap = cellRangeMap;
        this.columnErrorList = mode.getResponse().getErrorList();
        this.indexes = indexes;
        this.readHeadRownum = readHeadRownum;
    }

    /**
     * @param rownum 等于头读取行时返回bull
     */
    public ExcelResult<T> build(int rownum) {
        if (readHeadRownum == rownum)
            return null;
        Row row = mode.getSheet().getRow(rownum);
        if (row == null)
            return null;
        return build(row, mode.getResponse().getList(), classes);
    }

    /**
     * 构建表内全部内容
     * 
     * @throws ConverterExcelException
     */
    public ExcelListResult<T> build() throws ConverterExcelException {
        return buildList(0, sheetMaxRow);
    }

    public ExcelListResult<T> buildList(int start, int end) {
        columnErrorList.size();
        end = end < sheetMaxRow ? end : sheetMaxRow;
        start = 0 > start ? 0 : start;
        ExcelListResult<T> excelListResult = new ExcelListResult<T>(start, end, mode.getSheet(), mode);
        for (int i = 0; i < end; i++) {
            ExcelResult<T> t = build(i);
            if (t != null) {
                excelListResult.addResult(t);
            }
        }
        excelListResult.upDateErrorType();
        veriticationIndex(excelListResult);
        return excelListResult;
    }

    private void veriticationIndex(ExcelListResult<T> excelListResult) {
        ArrayList<CellAddressAndMessage> list = new ArrayList<CellAddressAndMessage>();
        List<LocationTag<T>> list1 = excelListResult.getRusultList();
        for (List<String> index : indexes) {
            List<QepeatResponse> qepeats = Compares.repeatDataAll(excelListResult.getRusultList(), (s) -> {
                String value = "";
                for (String string : index) {
                    if (s.getSetFiledNull().contains(string))
                        return null;
                    String methodName = "get" + StrUtil.upperFirst(string);
                    Object obj = ReflectUtil.invoke(s.getValue(), methodName);
                    value = value + obj.toString();
                }
                return value;
            });

            for (QepeatResponse qepeat : qepeats) {
                list.add(new CellAddressAndMessage(list1.get(qepeat.getIndex()).getRowmun(), 0,
                        ExcelExceptionType.INDEX_ERROR,
                        (qepeat.getQepeatIndex() != -1)
                                ? " 冲突的行号"
                                        + (excelListResult.getRusultList().get(qepeat.getQepeatIndex()).getRowmun() + 1)
                                : "冲突行"));
            }
        }
        if (list.size() != 0)
            excelListResult.getErrorType().add(ExcelExceptionType.INDEX_ERROR);
        excelListResult.getCellErrorList().addAll(list);
    }

    /**
     * 当所有内容均未匹配时，返回null;
     * 
     * @throws ConverterExcelException
     */
    private ExcelResult<T> build(Row row, List<CompareResponse<FieldDetail<T>, String>> list, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(row.getRowNum(), out);
        for (CompareResponse<FieldDetail<T>, String> compareResponse : list) {
            String methodName = "set" + ReflectLcUtils.upperCase(compareResponse.getSrcData().getFieldName());
            FieldDetail<T> fieldDetail = compareResponse.getSrcData();
            Cell cell = row.getCell(compareResponse.getDesIndex());
            ExcelConverterEntity excelRead = compareResponse.getSrcData().getExcelRead();
            if (cell == null) {
                if (excelRead.isCheck()) {
                    tag.getSetFiledNull().add(compareResponse.getSrcData().getFieldName());
                    CellAddressAndMessage cellAddressAndMessage = new CellAddressAndMessage(row.getRowNum(),
                            compareResponse.getDesIndex(), ExcelExceptionType.NOT_FIND_CHACK_VALUE, null);
                    checkErrorList.add(cellAddressAndMessage);
                }
                continue;
            }
            if (cell.toString().equals(""))
                if (excelRead.isCheck()) {
                    tag.getSetFiledNull().add(compareResponse.getSrcData().getFieldName());
                    checkErrorList.add(new CellAddressAndMessage(row.getRowNum(), compareResponse.getDesIndex(),
                            ExcelExceptionType.NOT_FIND_CHACK_VALUE, ""));
                    continue;
                }

            CellAddressAndMessage cellMessages = cellValueToClass(cell, cell.getCellType(), out, methodName,
                    fieldDetail, cell.getAddress());
            if (cellMessages.getEx() != ExcelExceptionType.SUCCESS && excelRead.isCheck()) {
                tag.getSetFiledNull().add(compareResponse.getSrcData().getFieldName());
                checkErrorList.add(new CellAddressAndMessage(row.getRowNum(), compareResponse.getDesIndex(),
                        cellMessages.getEx(), cellMessages.getMessage()));
                continue;
            }
        }
        return new ExcelResult<T>(row.getRowNum(), tag, checkErrorList);// size == 0 ? null : tag;
    }

    /**
     * 依照cellType内数据类型将其转换成set内参数1的数据类型
     * 
     * @param cell             存储数值cell对象
     * @param type             存储判断cell类型的对象
     * @param out              输出的对象的实例
     * @param setFunctionValue set方法的构造名称
     */
    public CellAddressAndMessage cellValueToClass(Cell cell, CellType type, Object out, String setFunctionValue,
            FieldDetail<T> fieldDetail, CellAddress cellAddress) {
        Class<?> paramterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (paramterClasses == null)
            return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                    ExcelExceptionType.NO_FIND_METHOD,
                    ExcelExceptionType.NO_FIND_METHOD.getTypeName() + ": " + setFunctionValue);
        Object value = null;
        if (fieldDetail.getRangeList().size() != 0) {
            if (!fieldDetail.getRangeList().contains(cell.toString())) {
                return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                        ExcelExceptionType.ENUM_ERROR, cell + " " + ExcelExceptionType.ENUM_ERROR.getTypeName()
                                + ",支持范围：" + fieldDetail.getRangeList());
            }
        }
        if (fieldDetail.getType() == FieldDetailType.LIST)
            paramterClasses = String.class;
        switch (type) {
        case NUMERIC:
            if (paramterClasses == String.class)
                invokeValue(out, setFunctionValue, cell.toString(), fieldDetail);
            else if (paramterClasses == Date.class)
                invokeValue(out, setFunctionValue, cell.getDateCellValue(), fieldDetail);
            else {
                value = converterRegistry.convert(paramterClasses, cell.getNumericCellValue());
                invokeValue(out, setFunctionValue, value, fieldDetail);
            }
            break;
        case STRING:
            String val = cell.toString().trim();
            if (val.length() > fieldDetail.getExcelRead().getMax()) {
                return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                        ExcelExceptionType.STRING_OUT_BOUNDS,
                        cell + " " + ExcelExceptionType.STRING_OUT_BOUNDS.getTypeName() + "，允许最大字符串长度"
                                + fieldDetail.getExcelRead().getMax());
            }
            if (paramterClasses.isEnum()) {
                value = EnumUtil.isEnumName(paramterClasses, val, fieldDetail.getEnumFileName());
                if (value == null)
                    return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                            ExcelExceptionType.ENUM_ERROR, cell + " " + ExcelExceptionType.ENUM_ERROR.getTypeName()
                                    + ",支持范围：" + EnumUtil.getEnumNames(paramterClasses, fieldDetail.getEnumFileName()));
            } else {
                try {
                    value = converterRegistry.convert(paramterClasses, val);
                } catch (NumberFormatException e) {
                    if (fieldDetail.getExcelRead() != null && fieldDetail.getExcelRead().isCheck())
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                                ExcelExceptionType.NUMBER_CONVERT_TYPE_ERROR, cell + " " + "转换失败数据：" + val);
                    return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                            ExcelExceptionType.SUCCESS);
                }
                if (value == null) {
                    if (fieldDetail.getExcelRead() != null && fieldDetail.getExcelRead().isCheck())
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                                ExcelExceptionType.DATE_CONVERT_TYPE_ERROR, cell + " " + "转换失败数据：" + val);
                    return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                            ExcelExceptionType.SUCCESS);
                }
            }
            invokeValue(out, setFunctionValue, value, fieldDetail);
            break;
        case FORMULA:
            return cellValueToClass(cell, cell.getCachedFormulaResultType(), out, setFunctionValue, fieldDetail,
                    cellAddress);
        case BLANK:
            CellType cellType = cell.getCellType();
            if (cellRangeMap != null) {
                CellAddress cellRangeAddress = cellRangeMap.get(cell.getAddress());
                if (cellRangeAddress == null) {
                    cellType = CellType.ERROR;
                } else {
                    cell = SheetUtil.getCellWithMerges(cell.getSheet(), cellRangeAddress.getRow(),
                            cellRangeAddress.getColumn());
                    if (cell == null)
                        cellType = CellType.ERROR;
                    else {
                        if (cell.getCellType() == CellType.BLANK)
                            cellType = CellType.ERROR;
                        else
                            cellType = cell.getCellType();
                    }
                }
            } else {
                cellType = CellType.ERROR;
            }
            return cellValueToClass(cell, cellType, out, setFunctionValue, fieldDetail, cellAddress);
        default:
            return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                    ExcelExceptionType.NO_CELL_TYPE_SUPPORT_TYPE, cell + " "
                            + ExcelExceptionType.NO_CELL_TYPE_SUPPORT_TYPE.getTypeName() + " : " + type);
        }
        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
    }

    /**
     * 生成list对象
     */
    private void invokeValue(Object out, String setFunctionValue, Object value, FieldDetail<T> fieldDetail) {
        if (value == null)
            return;
        if (fieldDetail.getType() == FieldDetailType.LIST) {
            String methodNameGet = "get" + ReflectLcUtils.upperCase(fieldDetail.getFieldName());
            ArrayList<String> list = ReflectUtil.invoke(out, methodNameGet);
            if (list == null) {
                list = new ArrayList<String>();
                ReflectUtil.invoke(out, setFunctionValue, list);
            }
            list.add(converterRegistry.convert(String.class, value));
            return;
        }
        ReflectUtil.invoke(out, setFunctionValue, value);
    }
}
