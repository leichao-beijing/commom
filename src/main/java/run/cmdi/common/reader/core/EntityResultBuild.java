package run.cmdi.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.util.CellAddress;
import run.cmdi.common.compare.Compares;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.compare.model.QepeatResponse;
import run.cmdi.common.io.EnumUtil;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.FieldDetail;
import run.cmdi.common.reader.model.HeadInfo;
import run.cmdi.common.reader.model.entity.CellAddressAndMessage;
import run.cmdi.common.reader.model.entity.EntityResult;
import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.core.Validator;
import run.cmdi.common.validator.core.ValidatorTools;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.*;

/**
 * @author leichao
 */
public class EntityResultBuild<T, PAGE, UNIT> implements EntityBuild<T, PAGE, UNIT> {
    private final Class<T> classes;
    private final HeadInfo<PAGE, UNIT> mode;
    private final List<List<String>> indexes;
    private final int readHeadRownum;
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    private final Map<String, FieldDetail> fieldMap;
    private final Validator validator;
    private final ReaderPoiConfig config;

    public EntityResultBuild(Class<T> clazz, HeadInfo<PAGE, UNIT> mode, List<List<String>> indexes, int readHeadRownum, ReaderPoiConfig config) throws ConverterException {
        this.classes = clazz;
        this.mode = mode;
        this.indexes = indexes;
        this.readHeadRownum = readHeadRownum;
        this.fieldMap = mode.getMap();
        this.validator = ValidatorTools.buildValidator(clazz);//new ValidatorTools(clazz);
        this.config = config;
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
        int row;
        if (!config.isStartRow())
            row = config.getStartRowNum();
        else
            row = readHeadRownum + 1;
        return build(row, mode.getReaderPage().length());
    }

    @Override
    public EntityResults<T, PAGE, UNIT> build(int start, int end) {
        end = Math.min(end, mode.getReaderPage().length());
        start = Math.max(0, start);
        EntityResults<T, PAGE, UNIT> entityResults = new EntityResults<T, PAGE, UNIT>(start, end, mode.getReaderPage(), mode);
        for (int i = start; i < end; i++) {
            EntityResult<T, PAGE, UNIT> t = build(i);
            if (t != null) {
                entityResults.addResult(t, config.isSkipErrorResult());
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
                    if (s.getFieldNull().contains(string))
                        continue;
                    String methodName = "get" + StrUtil.upperFirst(string);
                    Object obj = ReflectUtil.invoke(s.getValue(), methodName);
                    if (obj == null)
                        continue;
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
            entityResults.getErrorType().add(ConverterErrorType.INDEX_ERROR.getTypeName());
        entityResults.getCellErrorList().addAll(list);
    }


    /**
     * 当所有内容均未匹配时，返回null;
     */
    private EntityResult<T, PAGE, UNIT> build(RowInfo rowInfo, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(rowInfo.getRownum(), out);

        List<ValidatorFieldException> error = validator.validationMap(rowInfo.getData());
        error.forEach((val) -> { //TODO 数据校验层
            FieldDetail fieldDetail = fieldMap.get(val.getFieldName());
            if (fieldDetail == null) return;
            checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldDetail.getPosition(), val.getType(), val.getMessage()));
        });

        Iterator<Map.Entry<String, Object>> it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String name = next.getKey();
            Object value = next.getValue();
            String methodName = "set" + ReflectLcUtils.upperCase(name);
            FieldDetail fieldDetail = fieldMap.get(name);
            try {
                cellValueToClass(value, out, methodName, fieldDetail,
                        new CellAddress(rowInfo.getRownum(), fieldDetail.getPosition()));
            } catch (ValidatorException e) {
                checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldDetail.getPosition(), e.getType(), e.getMessage()));
                tag.getFieldNull().add(name);
            } catch (ConverterExcelException e) {
                if (validator.isConverter(name)) {
                    checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldDetail.getPosition(), e));
                    tag.getFieldNull().add(name);
                }
            }
            //fieldDetail.setConverterException(true);//初始化异常状态
        }
        return new EntityResult<T, PAGE, UNIT>(readHeadRownum, rowInfo.getRownum(), mode, tag, checkErrorList);// size == 0 ? null : tag;
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
                                 FieldDetail fieldDetail, CellAddress cellAddress) throws ConverterExcelException {
        if (value == null) return;
        Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (StringUtils.isEmpty(value) && fieldDetail.getType() != FieldDetailType.LIST)
            return;
        try {
            if (parameterClasses.isEnum()) {
                String methodName = (!fieldDetail.getEnumFieldName().equals("")) ? ReflectLcUtils.methodGetString(fieldDetail.getEnumFieldName()) : fieldDetail.getEnumFieldName();
                List<String> list = EnumUtil.getEnumNames((Class<Enum>) parameterClasses, methodName);
                if (list.contains(value)) {
                    Object en = EnumUtil.isEnumName(parameterClasses, value.toString(), methodName);
                    ReflectUtil.invoke(out, setFunctionValue, en);
                    return;
                } else//todo 处理
                    throw new ConverterExcelException(ConverterErrorType.ENUM_ERROR, fieldDetail.getMatchValue() + " 只能输入：" + list.toString());
            }

            if (!value.getClass().equals(parameterClasses)) {
                Object data;
                if (ConverterFieldDetail.IsInterface(parameterClasses, Date.class)) {
                    if (fieldDetail.getFormat() == null)
                        data = DateUtil.parse(value.toString());
                    else
                        data = DateUtil.parse(value.toString(), fieldDetail.getFormat().value());
                } else
                    data = converterRegistry.convert(parameterClasses, value);

                if (data == null && fieldDetail.isConverterException())
                    throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, "数据：" + value + " " + "转换为：" + parameterClasses.getSimpleName() + " 类型失败。" +
                            ((fieldDetail.getFormat() != null) ? "支持要求" + fieldDetail.getFormat() : ""));
                else
                    ReflectUtil.invoke(out, setFunctionValue, data);
            } else
                ReflectUtil.invoke(out, setFunctionValue, value);
        } catch (Exception e) {
            if (fieldDetail.isConverterException())
                throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, e.getMessage());
        }
    }

    private RowInfo readRow(int rownum, ReaderPage readerPage) {
        List<Object> row = readerPage.readRowList(rownum);
        HashMap<String, Object> map = buildRowMap(row);
        return new RowInfo(rownum, map);
    }

    private HashMap<String, Object> buildRowMap(List<Object> row) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        fieldMap.forEach((key, value) -> {
            try {

                if (value.getType() == FieldDetailType.SINGLE) {
                    Object data = row.get(value.getPosition());
                    map.put(key, data);
                } else {
                    List list = new ArrayList();
                    map.put(key, list);
                    try {
                        Object data = row.get(value.getPosition());
                        list.add(data == null ? "" : data);
                    } catch (IndexOutOfBoundsException e) {
                        list.add("");
                    }
                    value.getOtherDetails().forEach((va) -> {
                        if (va.getMatchValue() == null) {
                            list.add("");
                            return;
                        }
                        try {
                            Object val = row.get(va.getPosition());
                            list.add(val == null ? "" : val);
                        } catch (IndexOutOfBoundsException e) {
                            list.add("");
                        }
                    });

                }

            } catch (IndexOutOfBoundsException e) {
                map.put(key, null);
            }
        });
        return map;
    }
}
