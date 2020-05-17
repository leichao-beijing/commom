package run.cmid.common.reader.core;

import java.util.*;

import org.apache.poi.ss.util.CellAddress;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.*;
import run.cmid.common.io.EnumUtil;
import run.cmid.common.reader.annotations.Method;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.entity.CellAddressAndMessage;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.model.entity.ExcelResult;
import run.cmid.common.reader.model.eumns.ConfigErrorType;
import run.cmid.common.reader.model.eumns.ExcelExceptionType;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * @author leichao
 */
public class EntityResultBuild<T> implements EntityBuild<T> {
    private final Class<T> classes;
    private final HeadInfo mode;
    private final List<LocationTagError<FieldDetail, ConverterExcelException>> columnErrorList;
    private final List<List<String>> indexes;
    private final int readHeadRownum;
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public EntityResultBuild(Class<T> classes, HeadInfo mode, List<List<String>> indexes, int readHeadRownum) {
        this.classes = classes;
        this.mode = mode;
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
        RowInfo rowInfo = readRow(rownum, mode.getReaderPage());
        if (rowInfo == null)
            return null;

        return build(rowInfo, classes);
    }

    /**
     * 构建表内全部内容
     *
     * @throws ConverterExcelException
     */
    public EntityResult<T> build() throws ConverterExcelException {
        return buildList(0, mode.getReaderPage().length());
    }

    public EntityResult<T> buildList(int start, int end) {
        //columnErrorList.size();
        end = Math.min(end, mode.getReaderPage().length());
        start = Math.max(0, start);
        EntityResult<T> entityResult = new EntityResult<T>(start, end, mode.getReaderPage(), mode);
        for (int i = 0; i < end; i++) {
            ExcelResult<T> t = build(i);
            if (t != null) {
                entityResult.addResult(t);
            }
        }
        entityResult.upDateErrorType();
        verificationIndex(entityResult);
        return entityResult;
    }

    private void verificationIndex(EntityResult<T> entityResult) {
        ArrayList<CellAddressAndMessage> list = new ArrayList<CellAddressAndMessage>();
        List<LocationTag<T>> list1 = entityResult.getResultList();
        for (List<String> index : indexes) {
            List<QepeatResponse> qepeats = Compares.repeatDataAll(entityResult.getResultList(), (s) -> {
                String value = "";
                for (String string : index) {
                    if (s.getFiledNull().contains(string))
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
                list.add(new CellAddressAndMessage(list1.get(qepeat.getIndex()).getPosition(), 0,
                        ExcelExceptionType.INDEX_ERROR,
                        (qepeat.getQepeatIndex() != -1)
                                ? " 冲突的行号"
                                + (entityResult.getResultList().get(qepeat.getQepeatIndex()).getPosition() + 1)
                                : "冲突行"));
            }
        }
        if (list.size() != 0)
            entityResult.getErrorType().add(ExcelExceptionType.INDEX_ERROR);
        entityResult.getCellErrorList().addAll(list);
    }


    /**
     * 当所有内容均未匹配时，返回null;
     *
     * @throws ConverterExcelException
     */
    private ExcelResult<T> build(RowInfo rowInfo, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(rowInfo.getRownum(), out);
        Iterator<Map.Entry<String, DataArray<Object, FieldDetail>>> it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DataArray<Object, FieldDetail>> next = it.next();
            String name = next.getKey();
            DataArray<Object, FieldDetail> value = next.getValue();
            String methodName = "set" + ReflectLcUtils.upperCase(name);
            if (value.getInfo().isNullCheck() && value.getValue() == null) {
                tag.getFiledNull().add(name);
                CellAddressAndMessage message = new CellAddressAndMessage(rowInfo.getRownum(),
                        value.getInfo().getColumn(), ExcelExceptionType.NOT_FIND_CHACK_VALUE);
                checkErrorList.add(message);
                continue;
            }
            Method[] methods = value.getInfo().getMethods();
            ArrayList<CellAddressAndMessage> megs = null;
            if (methods != null && methods.length != 0) {
                megs = new ArrayList<>();
                for (Method method : methods) {
                    DataArray<Object, FieldDetail> desValues = method.fieldName().equals("") ? value
                            : rowInfo.getData().get(method.fieldName());
                    CellAddressAndMessage mess = rangeValue(value, desValues, method,
                            new CellAddress(rowInfo.getRownum(), desValues.getInfo().getColumn()));
                    if (mess != null)
                        megs.add(mess);
                }
                if (megs.size() != 0) {
                    tag.getFiledNull().add(name);
                    checkErrorList.addAll(megs);
                    continue;
                }
            }
            if (value.getValue() != null && value.getValue().toString().length() > value.getInfo().getMax()) {
                tag.getFiledNull().add(name);
                CellAddressAndMessage message = new CellAddressAndMessage(rowInfo.getRownum(),
                        value.getInfo().getColumn(), ExcelExceptionType.STRING_OUT_BOUNDS,
                        value.getInfo().getMax() + "");
                tag.getFiledNull().add(name);
                checkErrorList.add(message);
                continue;
            }
            CellAddressAndMessage message = cellValueToClass(value.getValue(), out, methodName, value.getInfo(),
                    new CellAddress(rowInfo.getRownum(), value.getInfo().getColumn()));
            if (message.getEx() != ExcelExceptionType.SUCCESS) {
                tag.getFiledNull().add(name);
                checkErrorList.add(message);
                continue;
            }
        }
        return new ExcelResult<T>(rowInfo.getRownum(), tag, checkErrorList);// size == 0 ? null : tag;
    }


    /**
     * 当满足 Method.value 等于 srcValue 时 ， 执行 从 Method.fieldName对象中取到值 = desValue，与Method.compareValue 使用  Method.model进行比较。<br>
     * Method.fieldName 为 空 时，Method.fieldName= srcValue=desValue<br>
     * Method.value 为 空 时，srcValue等于任何值都会执行做后续比较判断。<br>
     *
     * @param srcValue 使用 {@linkplain Method} 所在field的信息
     * @param desValue 使用 {@linkplain Method} {@code fieldName()} 得到的信息
     * @param method
     * @param address
     */
    public CellAddressAndMessage rangeValue(DataArray<Object, FieldDetail> srcValue, DataArray<Object, FieldDetail> desValue, Method method, CellAddress address) {
        if (desValue.getValue() == null)
            return null;
        if (method.compareValue().length == 0)
            throw new ConverterExcelConfigException(ConfigErrorType.METHOD_VALUES_IS_EMPTY);
        if (!method.value().equals("") && !srcValue.getValue().equals(method.value()))
            return null;
        List<String> list = Arrays.asList(method.compareValue());
        if (desValue.getValue().equals("003"))
            System.err.println("222");
        switch (method.model()) {
            case EQUALS:
                if (desValue.getValue() != null && list.contains(desValue.getValue()))
                    return null;
                else
                    break;
            case INCLUDE:
                for (String val : list) {
                    if(desValue.getValue().toString().indexOf(val)!=-1)
                        return null;
                }
                break;
            case NO_EQUALS:
                if (!list.contains(desValue.getValue()))
                    return null;
                break;
            case NO_INCLUDE:
                boolean state = false;
                for (String val : list) {
                    if(desValue.getValue().toString().indexOf(val)==-1){
                        state = true;
                        break;
                    }
                }
                if (state)
                    return null;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + mode);
        }
        switch (method.exceptionType()) {
            case NUMBER:
                if (Validator.isNumber(desValue.getValue().toString()))
                    return null;
                break;
            default:
                break;
        }
        String mgs = null;
        if (desValue.equals(srcValue)) {
            mgs = desValue.getInfo().getMatchValue() +
                    " 列的值不能为: " + desValue.getValue() + " ,在 " + list+ " 的范围内不满足 " +method.model().getTypeName() +" 的判断条件";
        } else {
            mgs = "当 " + srcValue.getInfo().getMatchValue() + " 列的值等于 " + method.value() + " 时，" + desValue.getInfo().getMatchValue() +
                    " 列的值不能为: " + desValue.getValue() + " ,在 " + list+ " 的范围内不满足 " +method.model().getTypeName() +" 的判断条件";
        }
        CellAddressAndMessage message = new CellAddressAndMessage(address.getRow(), address.getColumn(),
                ExcelExceptionType.ENUM_ERROR, mgs);
        return message;
    }

    /**
     * 依照cellType内数据类型将其转换成set内参数1的数据类型
     *
     * @param value
     * @param fieldDetail
     * @param out              输出的对象的实例
     * @param setFunctionValue set方法的构造名称
     * @param cellAddress
     */
    public CellAddressAndMessage cellValueToClass(Object value, Object out, String setFunctionValue,
                                                  FieldDetail fieldDetail, CellAddress cellAddress) {
        Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (value == null && fieldDetail.getType() != FieldDetailType.LIST)
            return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
        if (fieldDetail.getType() != FieldDetailType.LIST)
            try {
                if (parameterClasses.isEnum()) {
                    String methodName = (fieldDetail.getEnumTypeNameFiledValue() != null) ? ReflectLcUtils.methodGetString(fieldDetail.getEnumTypeNameFiledValue()) : null;
                    List<String> list = EnumUtil.getEnumNames(parameterClasses, methodName);
                    if (list.contains(value)) {
                        Object en = EnumUtil.isEnumName(parameterClasses, value.toString(), methodName);
                        ReflectUtil.invoke(out, setFunctionValue, en);
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
                    } else
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                                ExcelExceptionType.ENUM_ERROR, list.toString());
                }

                if (!value.getClass().equals(parameterClasses)) {
                    Object data = converterRegistry.convert(parameterClasses, value);
                    if (data == null)
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                                ExcelExceptionType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + parameterClasses.getSimpleName() + " 类型失败。");
                    else {
                        ReflectUtil.invoke(out, setFunctionValue, data);
                        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
                    }
                } else
                    ReflectUtil.invoke(out, setFunctionValue, value);
            } catch (Exception e) {
                return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(),
                        ExcelExceptionType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + parameterClasses + " 类型失败。");
            }
        return new CellAddressAndMessage(cellAddress.getRow(), cellAddress.getColumn(), ExcelExceptionType.SUCCESS);
    }

    private RowInfo readRow(int rownum, ReaderPage readerPage) {
        List<Object> list = readerPage.readRowList(rownum);
        HashMap<String, DataArray<Object, FieldDetail>> map = buildRowMap(list, mode.getResponse().getList());
        return new RowInfo(rownum, map);
    }

    private HashMap<String, DataArray<Object, FieldDetail>> buildRowMap(List<Object> row,
                                                                        List<CompareResponse<FieldDetail, String>> list) {
        HashMap<String, DataArray<Object, FieldDetail>> map = new HashMap<String, DataArray<Object, FieldDetail>>();
        for (CompareResponse<FieldDetail, String> compareResponse : list) {
            FieldDetail detail = compareResponse.getSrcData();
            Object value = null;
            try {
                value = row.get(compareResponse.getDesIndex());
            } catch (IndexOutOfBoundsException e) {

            }
            if (value == null)
                map.put(detail.getFieldName(), new DataArray<Object, FieldDetail>(null, detail));
            else {
                DataArray<Object, FieldDetail> arrays = map.get(detail.getFieldName());
                if (arrays == null) {
                    map.put(detail.getFieldName(), new DataArray<Object, FieldDetail>(value, detail));
                } else
                    arrays.add(value, detail);
            }
        }
        return map;
    }
}
