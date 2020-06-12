package run.cmid.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.util.CellAddress;
import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.*;
import run.cmid.common.io.EnumUtil;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.annotations.Match;
import run.cmid.common.reader.annotations.Method;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.entity.CellAddressAndMessage;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.model.entity.EntityResults;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.validator.FiledValidator;

import java.util.*;

/**
 * @author leichao
 */
public class EntityResultBuild<T, PAGE, UNIT> implements EntityBuild<T, PAGE, UNIT> {
    private final Class<T> classes;
    private final HeadInfo<PAGE, UNIT> mode;
    private final List<LocationTagError<FieldDetail, ConverterExcelException>> columnErrorList;
    private final List<List<String>> indexes;
    private final int readHeadRownum;
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public EntityResultBuild(Class<T> classes, HeadInfo<PAGE, UNIT> mode, List<List<String>> indexes, int readHeadRownum) {
        this.classes = classes;
        this.mode = mode;
        this.columnErrorList = mode.getResponse().getErrorList();
        this.indexes = indexes;
        this.readHeadRownum = readHeadRownum;
    }

    /**
     * @param rownum 等于头读取行时返回bull
     */
    @Override
    public EntityResult<T, PAGE, UNIT> build(int rownum) {
        if (readHeadRownum == rownum)
            return null;
        RowInfo rowInfo = readRow(rownum, mode.getReaderPage());
        if (rowInfo == null)
            return null;

        return build(rowInfo, classes);
    }

    /**
     * 构建表内全部内容
     */
    @Override
    public EntityResults<T, PAGE, UNIT> build() {
        return build(0, mode.getReaderPage().length());
    }

    @Override
    public EntityResults<T, PAGE, UNIT> build(int start, int end) {
        end = Math.min(end, mode.getReaderPage().length());
        start = Math.max(0, start);
        EntityResults<T, PAGE, UNIT> entityResults = new EntityResults<T, PAGE, UNIT>(start, end, mode.getReaderPage(), mode);
        for (int i = 0; i < end; i++) {
            EntityResult<T, PAGE, UNIT> t = build(i);
            if (t != null) {
                entityResults.addResult(t);
            }
        }
        entityResults.upDateErrorType();
        verificationIndex(entityResults);
        return entityResults;
    }

    private void verificationIndex(EntityResults<T, PAGE, UNIT> entityResults) {
        ArrayList<CellAddressAndMessage> list = new ArrayList<CellAddressAndMessage>();
        List<LocationTag<T>> list1 = entityResults.getResultList();
        for (List<String> index : indexes) {
            List<QepeatResponse> qepeats = Compares.repeatDataAll(entityResults.getResultList(), (s) -> {
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
                        ConverterErrorType.INDEX_ERROR,
                        (qepeat.getQepeatIndex() != -1)
                                ? " 冲突的行号"
                                + (entityResults.getResultList().get(qepeat.getQepeatIndex()).getPosition() + 1)
                                : "冲突行"));
            }
        }
        if (list.size() != 0)
            entityResults.getErrorType().add(ConverterErrorType.INDEX_ERROR);
        entityResults.getCellErrorList().addAll(list);
    }


    /**
     * 当所有内容均未匹配时，返回null;
     */
    private EntityResult<T, PAGE, UNIT> build(RowInfo rowInfo, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(rowInfo.getRownum(), out);
        Iterator<Map.Entry<String, DataArray<Object, FieldDetail>>> it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DataArray<Object, FieldDetail>> next = it.next();
            DataArray<Object, FieldDetail> value = next.getValue();
            Match[] matches = value.getInfo().getMatch();
            if (matches.length != 0) {
                for (Match match : matches) {
                    try {
                        List<String> list = MatchValidator.validatorFiledMatches(match.filedMatches(), rowInfo);
                        if (list != null && list.size() == 0) {
                            if (value.getInfo().isConverterException())
                                value.getInfo().setConverterException(match.converterException());//对转换异常进行响应配置
                            continue;
                        }
                        MatchValidator.validatorFiledCompares(value, match.filedCompares(), rowInfo, (list == null) ? "" : list.toString());
                        MatchValidator.validatorMatch(value, match, rowInfo, (list == null) ? "" : list.toString());
                    } catch (ValidatorException e) {
                        if (!match.check() && e.getType() == ConverterErrorType.EMPTY)
                            break;//check==false 且  ConverterErrorType.EMPTY 时，忽略empty异常
                        checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), value.getInfo().getColumn(), e));
                    }
                    if (value.getInfo().isConverterException())
                        value.getInfo().setConverterException(match.converterException());//对转换异常进行响应配置
                }
            }
        }
        it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DataArray<Object, FieldDetail>> next = it.next();
            String name = next.getKey();
            DataArray<Object, FieldDetail> value = next.getValue();
            String methodName = "set" + ReflectLcUtils.upperCase(name);
            if (value.getValue() != null && value.getValue().toString().length() > value.getInfo().getMax()) {
                tag.getFiledNull().add(name);
                CellAddressAndMessage message = new CellAddressAndMessage(rowInfo.getRownum(),
                        value.getInfo().getColumn(), ConverterErrorType.STRING_OUT_BOUNDS,
                        value.getInfo().getMax() + "");
                tag.getFiledNull().add(name);
                checkErrorList.add(message);
                continue;
            }
            try {
                cellValueToClass(value.getValue(), out, methodName, value.getInfo(),
                        new CellAddress(rowInfo.getRownum(), value.getInfo().getColumn()));
            } catch (ValidatorException e) {
                checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), value.getInfo().getColumn(), e));
                tag.getFiledNull().add(name);
            }
            value.getInfo().setConverterException(true);//初始化异常状态
        }
        return new EntityResult<T, PAGE, UNIT>(readHeadRownum, rowInfo.rownum, mode, tag, checkErrorList);// size == 0 ? null : tag;
    }


    /**
     * 当满足 Method.value 等于 srcValue 时 ， 执行 从 Method.fieldName对象中取到值 = desValue，与Method.compareValue 使用  Method.model进行比较。<br>
     * Method.fieldName 为 空 时，Method.fieldName= srcValue=desValue<br>
     * Method.value 为 空 时，srcValue等于任何值都会执行做后续比较判断。<br>
     *
     * @param srcValue 使用 {@linkplain Method} 所在field的信息
     * @param desValue 使用 {@linkplain Method} {@code fieldName()} 得到的信息。
     * @param method
     * @param address
     */
    public void rangeValue(DataArray<Object, FieldDetail> srcValue, DataArray<Object, FieldDetail> desValue, Method method, CellAddress address) {
        //当{des}列的值{desValue}满足在{method.computeValue}范围内满足{method.mode}条件时，
        //{src}列的值[不能没有数据]or[不能是{srcValue}]
        //new StringBuffer()
        boolean equalState = true;
        if (!desValue.getInfo().getFieldName().equals(srcValue.getInfo().getFieldName())) {
            equalState = false;
            if (!FiledValidator.mode(desValue.getValue(), method.compareValue(), method.fieldNameModel())) {//通过des条件时，不进行判断，单空des可以通过
                return;
            }
        }
        String equalMgs = "";
        if (!equalState) {
            equalMgs = ((!StringUtils.isEmpty(desValue.getValue())) ? (FiledValidator.headMessage(desValue.getInfo().getMatchValue(), desValue.getValue()) +
                    FiledValidator.message(method.fieldNameModel(), method.compareValue(), true)) + "时，" : "");
        }
        boolean state = FiledValidator.mode(srcValue.getValue(), method.value(), method.model());
        if (!state || (method.check() && StringUtils.isEmpty(srcValue.getValue()))) {
            if (StringUtils.isEmpty(srcValue.getValue()))
                throw new ValidatorException(ConverterErrorType.NOT_FIND_CHECK_VALUE, equalMgs + "<" + srcValue.getInfo().getMatchValue() + "> 不能没有数据");
            String mgs;
            if (equalState) {
                mgs = FiledValidator.headMessage(srcValue.getInfo().getMatchValue(), method.fieldNameModel()) +
                        FiledValidator.message(method.fieldNameModel(), method.compareValue(), true) + " 和 " +
                        FiledValidator.message(method.model(), method.value(), false);
            } else {
                mgs = equalMgs + "时，" +
                        FiledValidator.headMessage(srcValue.getInfo().getMatchValue(), srcValue.getValue()) +
                        FiledValidator.message(method.model(), method.value(), false);
            }
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, mgs);
        }
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
    public void cellValueToClass(Object value, Object out, String setFunctionValue,
                                 FieldDetail fieldDetail, CellAddress cellAddress) {
        Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (StringUtils.isEmpty(value) && fieldDetail.getType() != FieldDetailType.LIST)
            return;
        if (fieldDetail.getType() != FieldDetailType.LIST)
            try {
                if (parameterClasses.isEnum()) {
                    String methodName = (fieldDetail.getEnumTypeNameFiledValue() != null) ? ReflectLcUtils.methodGetString(fieldDetail.getEnumTypeNameFiledValue()) : null;
                    List<String> list = EnumUtil.getEnumNames(parameterClasses, methodName);
                    if (list.contains(value)) {
                        Object en = EnumUtil.isEnumName(parameterClasses, value.toString(), methodName);
                        ReflectUtil.invoke(out, setFunctionValue, en);
                        return;
                    } else
                        throw new ValidatorException(ConverterErrorType.ENUM_ERROR, list.toString());
                }

                if (!value.getClass().equals(parameterClasses)) {
                    Object data = null;
                    if (ConverterFieldDetail.IsInterface(parameterClasses, Date.class)) {
                        if (fieldDetail.getFormat() == null)
                            data = DateUtil.parse(value.toString());
                        else
                            data = DateUtil.parse(value.toString(), fieldDetail.getFormat().value());
                    } else
                        data = converterRegistry.convert(parameterClasses, value);

                    if (data == null && fieldDetail.isConverterException())
                        throw new ValidatorException(ConverterErrorType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + parameterClasses.getSimpleName() + " 类型失败。" +
                                ((fieldDetail.getFormat() != null) ? "支持要求" + fieldDetail.getFormat() : ""));
                    else {
                        ReflectUtil.invoke(out, setFunctionValue, data);
                        return;
                    }
                } else
                    ReflectUtil.invoke(out, setFunctionValue, value);
            } catch (Exception e) {
                if (fieldDetail.isConverterException())
                    throw new ValidatorException(ConverterErrorType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + parameterClasses + " 类型失败。");
            }
    }

    private RowInfo readRow(int rownum, ReaderPage readerPage) {
        List<Object> row = readerPage.readRowList(rownum);
        HashMap<String, DataArray<Object, FieldDetail>> map = buildRowMap(row, mode.getResponse().getList());
        return new RowInfo(rownum, map);
    }

    private HashMap<String, DataArray<Object, FieldDetail>> buildRowMap(List<Object> row,
                                                                        List<CompareResponse<FieldDetail, String>> list) {
        HashMap<String, DataArray<Object, FieldDetail>> map = new HashMap<String, DataArray<Object, FieldDetail>>();

        for (CompareResponse<FieldDetail, String> compareResponse : list) {
            FieldDetail detail = compareResponse.getSrcData();
            if (row == null) {
                map.put(detail.getFieldName(), new DataArray<Object, FieldDetail>(null, detail));
                continue;
            }
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
