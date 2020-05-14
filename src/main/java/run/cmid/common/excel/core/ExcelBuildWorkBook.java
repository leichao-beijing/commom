package run.cmid.common.excel.core;

import java.util.ArrayList;
import java.util.Arrays;
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
import run.cmid.common.compare.model.DataArray;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.compare.model.LocationTagError;
import run.cmid.common.compare.model.QepeatResponse;
import run.cmid.common.excel.annotations.Method;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.SheetModel;
import run.cmid.common.excel.model.entity.CellAddressAndMessage;
import run.cmid.common.excel.model.entity.ExcelListResult;
import run.cmid.common.excel.model.entity.ExcelResult;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;
import run.cmid.common.excel.model.eumns.ExcelReadType;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.utils.ReflectLcUtils;

/**
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
                    if (obj == null)
                        return null;
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

    private HashMap<String, DataArray<Object, FieldDetail<T>>> buildRowMap(Row row,
            List<CompareResponse<FieldDetail<T>, String>> list) {
        HashMap<String, DataArray<Object, FieldDetail<T>>> map = new HashMap<String, DataArray<Object, FieldDetail<T>>>();
        for (CompareResponse<FieldDetail<T>, String> compareResponse : list) {
            FieldDetail<T> detail = compareResponse.getSrcData();
            Cell cell = row.getCell(compareResponse.getDesIndex());
            if (cell == null) {
                map.put(detail.getFieldName(), null);
                continue;
            }
            detail.setColumn(compareResponse.getDesIndex());
            buildCellMap(map, detail.getFieldName(), cell.getCellType(), cell, detail);
        }
        return map;
    }

    private void buildCellMap(HashMap<String, DataArray<Object, FieldDetail<T>>> data, String name, CellType type,
            Cell cell, FieldDetail<T> detail) {
        switch (type) {
        case _NONE:
            data.put(name, new DataArray<Object, FieldDetail<T>>(null, detail));
            break;
        case BLANK:
            if (cellRangeMap != null) {
                CellAddress cellRangeAddress = cellRangeMap.get(cell.getAddress());
                if (cellRangeAddress != null) {
                    cell = SheetUtil.getCellWithMerges(cell.getSheet(), cellRangeAddress.getRow(),
                            cellRangeAddress.getColumn());
                    if (cell == null)
                        type = CellType.ERROR;
                    else {
                        if (cell.getCellType() == CellType.BLANK)
                            type = CellType.ERROR;
                        else
                            type = cell.getCellType();
                    }
                    buildCellMap(data, name, type, cell, detail);
                    break;
                }
            }
            data.put(name, new DataArray<Object, FieldDetail<T>>(null, detail));
            break;
        case BOOLEAN:
            data.put(name, new DataArray<Object, FieldDetail<T>>(cell.getBooleanCellValue(), detail));
            break;
        case ERROR:
            data.put(name, new DataArray<Object, FieldDetail<T>>(null, detail));
            break;
        case FORMULA:
            CellType formulaType = cell.getCachedFormulaResultType();
            if (formulaType == CellType.FORMULA) {
                buildCellMap(data, name, CellType.ERROR, cell, detail);
                break;
            }
            buildCellMap(data, name, formulaType, cell, detail);
            break;
        case NUMERIC:
            data.put(name, new DataArray<Object, FieldDetail<T>>(cell.getNumericCellValue(), detail));
            break;
        case STRING:
            data.put(name, new DataArray<Object, FieldDetail<T>>(cell.toString(), detail));
            break;
        }
    }

    /**
     * 当所有内容均未匹配时，返回null;
     *
     * @throws ConverterExcelException
     */
    private ExcelResult<T> build(Row row, List<CompareResponse<FieldDetail<T>, String>> list, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        HashMap<String, DataArray<Object, FieldDetail<T>>> map = buildRowMap(row, list);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(row.getRowNum(), out);
        for (CompareResponse<FieldDetail<T>, String> compareResponse : list) {
            String methodName = "set" + ReflectLcUtils.upperCase(compareResponse.getSrcData().getFieldName());
            // Cell cell = row.getCell(compareResponse.getDesIndex());
            FieldDetail<T> detail = compareResponse.getSrcData();
            DataArray<Object, FieldDetail<T>> value = map.get(detail.getFieldName());
            Method[] methods = detail.getMethods();
            boolean state = true;
            if (methods != null)
                for (Method method : methods) {
                    DataArray<Object, FieldDetail<T>> tmp = method.fieldName().equals("") ? value
                            : map.get(method.fieldName());
                    if (!rangeValue(tmp.getData1(), method.values(), method.model(),
                            new CellAddress(row.getRowNum(), tmp.getData2().getColumn()), checkErrorList)) {
                        state = false;
                        break;
                    }
                }
            if (!state)
                continue;
            CellAddressAndMessage messgae = cellValueToClass(value.getData1(), out, methodName, detail,
                    new CellAddress(row.getRowNum(), compareResponse.getDesIndex()));
            if (messgae.getEx() != ExcelExceptionType.SUCCESS) {
                tag.getSetFiledNull().add(compareResponse.getSrcData().getFieldName());
                checkErrorList.add(messgae);
                continue;
            }
        }
        return new ExcelResult<T>(row.getRowNum(), tag, checkErrorList);// size == 0 ? null : tag;
    }

    public boolean rangeValue(Object value, String[] range, ExcelReadType mode, CellAddress address,
            List<CellAddressAndMessage> checkErrorList) {
        List<String> list = Arrays.asList(range);
        if(value != null&&value.equals("004"))
            System.err.println(value);
        switch (mode) {
        case EQUALS:
            if (list.size() == 0 && value == null)
                return true;
            else if (value != null && list.contains(value))
                return true;
            else
                break;
        case INCLUDE:
            if (value == null) {
                break;
            }
            for (String val : list) {
                if (val.startsWith(value.toString()))
                    return true;
            }
            break;
        case NO_EQUALS:
            if (list.size() == 0 && value != null)
                return true;
            else if (value != null && !list.contains(value))
                return true;
            break;
        case NO_INCLUDE:
            if (value == null) {
                break;
            }
            boolean state = false;
            for (String val : list) {
                if (val.startsWith(value.toString())) {
                    state = true;
                    break;
                }
            }
            if (state)
                return true;
            break;
        default:
            throw new IllegalArgumentException("Unexpected value: " + mode);
        }
//        String mgs = "数据 :" + value + " 不满足 " + ((list.size() == 0) ? " null " : "范围：" + list) + "。"
//                + mode.getTypeName() + "的条件";
        String mgs = "不能为：" + value + ((list.size() == 0) ? "" : "范围：" + list + " 条件" + mode.getTypeName());
        CellAddressAndMessage message = new CellAddressAndMessage(address.getRow(), address.getColumn(),
                ExcelExceptionType.ENUM_ERROR, mgs);
        checkErrorList.add(message);
        return false;
    }

    /**
     * 依照cellType内数据类型将其转换成set内参数1的数据类型
     *
     * @param cell             存储数值cell对象
     * @param type             存储判断cell类型的对象
     * @param out              输出的对象的实例
     * @param setFunctionValue set方法的构造名称
     */
    public CellAddressAndMessage cellValueToClass(Object value, Object out, String setFunctionValue,
            FieldDetail<T> fieldDetail, CellAddress cellAddress) {
        Class<?> paramterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);

        if (value == null && fieldDetail.getType() != FieldDetailType.LIST)
            return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
        if (fieldDetail.getType() == FieldDetailType.LIST)
            paramterClasses = String.class;
        else {
            try {
                value = converterRegistry.convert(paramterClasses, value);
            } catch (Exception e) {
                return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                        ExcelExceptionType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + paramterClasses + " 类型失败。");
            }
        }
        invokeValue(out, setFunctionValue, value, fieldDetail);
        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
    }

    /**
     * 生成list对象
     */
    private void invokeValue(Object out, String setFunctionValue, Object value, FieldDetail<T> fieldDetail) {
        if (fieldDetail.getType() == FieldDetailType.LIST) {
            String methodNameGet = "get" + ReflectLcUtils.upperCase(fieldDetail.getFieldName());
            List<String> list = ReflectUtil.invoke(out, methodNameGet);
            if (list == null) {
                list = new ArrayList<String>();
                ReflectUtil.invoke(out, setFunctionValue, list);
            }
            if (value == null)
                list.add("");
            else
                list.add(value.toString());
            return;
        }
        ReflectUtil.invoke(out, setFunctionValue, value);
    }
}
