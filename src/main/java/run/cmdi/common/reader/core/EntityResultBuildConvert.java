package run.cmdi.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.io.EnumUtil;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.entity.CellAddressAndMessage;
import run.cmdi.common.reader.model.entity.EntityResultConvert;
import run.cmdi.common.reader.model.entity.EntityResultsConvert;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.core.Validator;
import run.cmdi.common.validator.core.ValidatorTools;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author leichao
 */
public class EntityResultBuildConvert<T, PAGE, UNIT> {
    private final Class<T> classes;
    private final FieldInfos filedInfos;

    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    private final Validator validator;
    private final ReaderPoiConfig config;

    public EntityResultBuildConvert(Class<T> clazz, FieldInfos filedInfos, ReaderPoiConfig config) throws ConverterException {
        this.classes = clazz;
        this.filedInfos = filedInfos;
        this.validator = ValidatorTools.buildValidator(clazz);//new ValidatorTools(clazz);
        this.config = config;
    }

    /**
     * @param rownum 等于头读取行时返回bull
     */
    //@Override
    public EntityResultConvert<T, PAGE, UNIT> build(int rownum) {
        if (filedInfos.getReadHeadRownum() == rownum)
            return null;
        RowInfo rowInfo = readRow(rownum);
        if (rowInfo == null)
            return null;

        return build(rowInfo, classes);
    }

    /**
     * 构建表内全部内容
     */
    // @Override
    public EntityResultsConvert<T, PAGE, UNIT> build() {
        int row;
        if (!config.isStartRow())
            row = config.getStartRowNum();
        else
            row = filedInfos.getReadHeadRownum() + 1;
        return build(row, filedInfos.getPage().size());
    }

    //@Override
    public EntityResultsConvert<T, PAGE, UNIT> build(int start, int end) {
        end = Math.min(end, filedInfos.getPage().size());
        start = Math.max(0, start);
        EntityResultsConvert<T, PAGE, UNIT> entityResults = new EntityResultsConvert<T, PAGE, UNIT>(start, end, filedInfos);
        for (int i = start; i < end; i++) {
            EntityResultConvert<T, PAGE, UNIT> t = build(i);
            if (t != null) {
                entityResults.addResult(t, config.isSkipErrorResult());
            }
            System.err.println("");
        }
        entityResults.upDateErrorType();
        verificationIndex(entityResults);
        return entityResults;
    }

    private List<String> indexesValue(T t, List<List<String>> indexes) {
        List<String> list = new ArrayList<>();
        indexes.forEach(val -> {
            list.add(indexValue(t, val));
        });
        return list;
    }

    private String indexValue(T t, List<String> index) {
        List<String> list = new ArrayList<>();
        index.forEach(val -> {
            String methodName = "get" + StrUtil.upperFirst(val);
            Object obj = ReflectUtil.invoke(t, methodName);
            if (obj == null)
                list.add(null);
            else
                list.add(obj.toString());
        });

        return list.toString();
    }

    private void verificationIndex(EntityResultsConvert<T, PAGE, UNIT> entityResults) {
        ArrayList<CellAddressAndMessage> list = new ArrayList<CellAddressAndMessage>();
        List<LocationTag<T>> resultValues = entityResults.getResultList();
        HashMap<Integer, HashMap<String, Integer>> overMap = new HashMap<>();
        for (LocationTag<T> resultValue : resultValues) {
            List<String> indexValues = indexesValue(resultValue.getValue(), filedInfos.getConfig().getListIndex());
            for (int i = 0; i < indexValues.size(); i++) {
                HashMap<String, Integer> map = overMap.get(i);
                if (map == null) overMap.put(i, map = new HashMap<>());
                Integer overLine = map.get(indexValues.get(i));
                if (overLine != null) {
                    List<String> indexList = filedInfos.getConfig().getListIndex().get(i);
                    List<String> indexName = IntStream.range(0, indexList.size()).mapToObj(val -> filedInfos.getFileInfo(indexList.get(val)).getName()).collect(Collectors.toList());
                    list.add(new CellAddressAndMessage(resultValue.getPosition(), 0,
                            ConverterErrorType.INDEX_ERROR, "匹配冲突的内容:" + indexName + "." + resultValue.getPosition() + "行与行号:" + overLine + " 冲突."));
                }
            }
        }
        if (list.size() != 0)
            entityResults.getErrorType().add(ConverterErrorType.INDEX_ERROR.getTypeName());
        entityResults.getCellErrorList().addAll(list);
    }


    /**
     * 当所有内容均未匹配时，返回null;
     */
    private EntityResultConvert<T, PAGE, UNIT> build(RowInfo rowInfo, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        List<CellAddressAndMessage> checkErrorList = new ArrayList<CellAddressAndMessage>();
        LocationTag<T> tag = new LocationTag<T>(rowInfo.getRownum(), out);

        List<ValidatorFieldException> error = validator.validationMap(rowInfo.getData());
        error.forEach((val) -> { //TODO 数据校验层
            FieldInfo fieldInfo = filedInfos.getFileInfo(val.getFieldName());
            if (fieldInfo == null) return;
            checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldInfo.getIndex(), val.getType(), val.getMessage()));
        });

        Iterator<Map.Entry<String, Object>> it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String name = next.getKey();
            Object value = next.getValue();
            String methodName = "set" + ReflectLcUtils.upperCase(name);
            FieldInfo fieldInfo = filedInfos.getFileInfo(name);//; fieldMap.get(name);
            try {
                cellValueToClass(value, out, methodName, fieldInfo);
            } catch (ValidatorException e) {
                checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldInfo.getIndex(), e.getType(), e.getMessage()));
                tag.getFieldNull().add(name);
            } catch (ConverterExcelException e) {
                if (validator.isConverter(name)) {
                    checkErrorList.add(new CellAddressAndMessage(rowInfo.getRownum(), fieldInfo.getIndex(), e));
                    tag.getFieldNull().add(name);
                }
            }
        }
        return new EntityResultConvert(rowInfo.getRownum(), tag, checkErrorList);// size == 0 ? null : tag;
    }

    /**
     * 依照cellType内数据类型将其转换成set内参数1的数据类型
     *
     * @param value
     * @param out              输出的对象的实例
     * @param setFunctionValue set方法的构造名称
     * @param fieldInfo
     */
    public void cellValueToClass(Object value, Object out, String setFunctionValue,
                                 FieldInfo fieldInfo) throws ConverterExcelException {
        if (value == null) return;
        Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (StringUtils.isEmpty(value) && fieldInfo.getType() != FieldDetailType.LIST)
            return;
        try {
            if (parameterClasses.isEnum()) {
                String methodName = (!fieldInfo.getEnumFieldName().equals("")) ? ReflectLcUtils.methodGetString(fieldInfo.getEnumFieldName()) : fieldInfo.getEnumFieldName();
                List<String> list = EnumUtil.getEnumNames((Class<Enum>) parameterClasses, methodName);
                if (list.contains(value)) {
                    Object en = EnumUtil.isEnumName(parameterClasses, value.toString(), methodName);
                    ReflectUtil.invoke(out, setFunctionValue, en);
                    return;
                } else//todo 处理
                    throw new ConverterExcelException(ConverterErrorType.ENUM_ERROR, fieldInfo.getName() + " 只能输入：" + list.toString());
            }

            if (!value.getClass().equals(parameterClasses)) {
                Object data;
                if (ConverterFieldDetail.IsInterface(parameterClasses, Date.class)) {
                    if (fieldInfo.getFormatDate() == null)
                        data = DateUtil.parse(value.toString());
                    else
                        data = DateUtil.parse(value.toString(), fieldInfo.getFormatDate());
                } else
                    data = converterRegistry.convert(parameterClasses, value);
                if (data != null)
                    ReflectUtil.invoke(out, setFunctionValue, data);
            } else
                ReflectUtil.invoke(out, setFunctionValue, value);
        } catch (Exception e) {
            fieldInfo.exception(e);
        }
    }

    private RowInfo readRow(int rownum) {
        List row = filedInfos.getPage().getValues(rownum);
        Map<String, Object> map = buildRowMap(row);
        return new RowInfo(rownum, map);
    }

    private Map<String, Object> buildRowMap(List<Object> row) {
        Map<String, Object> mapInfo = new HashMap<>();
        filedInfos.getMap().forEach((key, info) -> {
            if (info.getType() == FieldDetailType.SINGLE)
                mapInfo.put(info.getFileName(), row.get(key));
            else if (info.getType() == FieldDetailType.LIST) {
                List value = (List) mapInfo.get(info.getFileName());
                if (value == null) mapInfo.put(info.getFileName(), value = new ArrayList<>());
                if (info.getListIndex() == value.size())
                    value.add(row.get(info.getListIndex()));
                else if (info.getListIndex() > value.size()) {
                    int i = info.getListIndex() - value.size();
                    for (int i1 = 0; i1 < i; i1++)
                        value.add("");
                    value.add(row.get(key));
                }else
                    value.set(info.getListIndex(),row.get(key));
            }
        });
        return mapInfo;
    }
}
