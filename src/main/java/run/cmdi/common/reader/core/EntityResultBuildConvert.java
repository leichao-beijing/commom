package run.cmdi.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.io.EnumUtil;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.FindFieldInfo;
import run.cmdi.common.reader.model.FindFieldInfos;
import run.cmdi.common.reader.model.entity.CellAddressAndMessage;
import run.cmdi.common.reader.model.entity.EntityResultConvert;
import run.cmdi.common.reader.model.entity.EntityResultsConvert;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.utils.DynamicList;
import run.cmdi.common.utils.MapUtils;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.ValidatorFieldException;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author leichao
 */
public class EntityResultBuildConvert<T> {
    private final Class<T> classes;
    private final FindFieldInfos filedInfos;
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public EntityResultBuildConvert(Class<T> clazz, FindFieldInfos filedInfos) throws ConverterException {
        this.classes = clazz;
        this.filedInfos = filedInfos;
    }

    /**
     * @param rownum 等于头读取行时返回bull
     */
    public EntityResultConvert<T> build(int rownum) {
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
    public EntityResultsConvert<T> build() {
        int row;
        if (filedInfos.getStartRow() != -1 && filedInfos.getReadHeadRownum() != filedInfos.getStartRow())
            row = filedInfos.getStartRow();
        else
            row = filedInfos.getReadHeadRownum() + 1;
        return build(row, filedInfos.getPage().size());
    }

    public EntityResultsConvert<T> build(int start, int end) {
        end = Math.min(end, filedInfos.getPage().size());
        start = Math.max(0, start);
        EntityResultsConvert<T> entityResults = new EntityResultsConvert<T>(start, end, filedInfos);
        for (int i = start; i < end; i++) {
            EntityResultConvert<T> t = build(i);
            if (t != null) {
                entityResults.addResult(t, filedInfos.isSkipErrorResult());
            }
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

    private void verificationIndex(EntityResultsConvert<T> entityResults) {
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
                } else
                    map.put(indexValues.get(i), i);
            }
        }
        if (list.size() != 0)
            entityResults.getErrorType().add(ConverterErrorType.INDEX_ERROR.getTypeName());
        entityResults.getHeardErrorList().addAll(list);
    }


    /**
     * 当所有内容均未匹配时，返回null;
     */
    private EntityResultConvert<T> build(RowInfo rowInfo, Class<T> classes) {
        T out = ReflectUtil.newInstance(classes);
        Map<Integer, CellAddressAndMessage> checkErrorMap = new HashMap<>();
        //List<ValidatorFieldException> list = new ArrayList<>();//存储无法查询到的单元信息
        LocationTag<T> tag = new LocationTag<T>(rowInfo.getRownum(), out);

        List<ValidatorFieldException> error = filedInfos.getValidator().validationMap(rowInfo.getData());
        error.forEach((val) -> { //TODO 数据校验层
            FindFieldInfo findFieldInfo = filedInfos.getFileInfo(val.getFieldName());
            if (findFieldInfo == null) return;
            MapUtils.lineMap(checkErrorMap, findFieldInfo.getAddress(), (value) -> {
//                try {
                    if (value == null)
                        return new CellAddressAndMessage(rowInfo.getRownum(), -1, val.getType(), val.getMessage());
//                } catch (Exception e) {
//                    throw e;
//                }
//                if (value == null)
//                    list.add(val);
//                else
                    value.add(val.getType(), val.getMessage());
                return value;
            });
        });

        Iterator<Map.Entry<String, Object>> it = rowInfo.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String name = next.getKey();
            Object value = next.getValue();
            String methodName = "set" + ReflectLcUtils.upperCase(name);
            FindFieldInfo findFieldInfo = filedInfos.getFileInfo(name);//; fieldMap.get(name);
            try {
                cellValueToClass(value, out, methodName, findFieldInfo);
            } catch (ValidatorException e) {
                MapUtils.lineMap(checkErrorMap, findFieldInfo.getIndex(), (vv) -> {
                    if (vv == null)
                        return new CellAddressAndMessage(rowInfo.getRownum(), findFieldInfo.getIndex(), e.getType(), e.getMessage());
                    vv.add(e.getType(), e.getMessage());
                    return vv;
                });
                //checkErrorMap.add(new CellAddressAndMessage(rowInfo.getRownum(), findFieldInfo.getIndex(), e.getType(), e.getMessage()));
                tag.getFieldNull().add(name);
            } catch (ConverterExcelException e) {
                if (filedInfos.getValidator().isConverter(name)) {
                    MapUtils.lineMap(checkErrorMap, findFieldInfo.getAddress(), (vv) -> {
                        if (vv == null)
                            return new CellAddressAndMessage(rowInfo.getRownum(), findFieldInfo.getAddress(), e.getType(), "不能输入这个值:" + value);
                        vv.add(e.getType(), e.getMessage());
                        return vv;
                    });
//                    checkErrorMap.add(new CellAddressAndMessage(rowInfo.getRownum(), findFieldInfo.getIndex(), e));
                    tag.getFieldNull().add(name);
                }
            }
        }
        return new EntityResultConvert(rowInfo.getRownum(), tag, checkErrorMap);// size == 0 ? null : tag;
    }

    /**
     * 依照cellType内数据类型将其转换成set内参数1的数据类型
     *
     * @param value
     * @param out              输出的对象的实例
     * @param setFunctionValue set方法的构造名称
     * @param findFieldInfo
     */
    public void cellValueToClass(Object value, Object out, String setFunctionValue,
                                 FindFieldInfo findFieldInfo) throws ConverterExcelException {
        if (value == null) return;
        Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(out.getClass(), setFunctionValue);
        if (StringUtils.isEmpty(value) && findFieldInfo.getType() != FieldDetailType.LIST)
            return;
        try {
            if (parameterClasses.isEnum()) {
                String methodName = (!findFieldInfo.getEnumFieldName().equals("")) ? ReflectLcUtils.methodGetString(findFieldInfo.getEnumFieldName()) : findFieldInfo.getEnumFieldName();
                List<String> list = EnumUtil.getEnumNames((Class<Enum>) parameterClasses, methodName);
                if (list.contains(value)) {
                    Object en = EnumUtil.isEnumName(parameterClasses, value.toString(), methodName);
                    ReflectUtil.invoke(out, setFunctionValue, en);
                    return;
                } else//todo 处理
                    throw new ConverterExcelException(ConverterErrorType.ENUM_ERROR,
                            ValueFieldName.build(findFieldInfo.getFieldName(), findFieldInfo.getName(), value).toString() +
                                    " 只能输入：" + list.toString());
            }
            if (!value.getClass().equals(parameterClasses)) {
                Object data;
                if (ReflectLcUtils.IsInterface(parameterClasses, Date.class)) {
                    if (findFieldInfo.getFormatDate() == null)
                        data = DateUtil.parse(value.toString());
                    else
                        data = DateUtil.parse(value.toString(), findFieldInfo.getFormatDate());
                } else
                    data = converterRegistry.convert(parameterClasses, value);
                if (data != null)
                    ReflectUtil.invoke(out, setFunctionValue, data);
            } else
                ReflectUtil.invoke(out, setFunctionValue, value);
        } catch (Exception e) {
            //e.printStackTrace();
            findFieldInfo.exception(e);
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
            if (info.getType() != FieldDetailType.LIST) {
                if (row.size() <= key) return;// OutOfBounds
                mapInfo.put(info.getFieldName(), row.get(key));
            } else {

                DynamicList value = (DynamicList) mapInfo.get(info.getFieldName());
                if (value == null) mapInfo.put(info.getFieldName(), value = new DynamicList(""));
                try {
                    Object result = row.get(key);
                    if (result != null)
                        value.dynamicAdd(info.getIndex(), row.get(key));
                    else
                        value.dynamicAdd(info.getIndex(), "");

                } catch (IndexOutOfBoundsException e) {
                    value.dynamicAdd(info.getIndex(), "");
                }
            }
        });
        return mapInfo;
    }
}
