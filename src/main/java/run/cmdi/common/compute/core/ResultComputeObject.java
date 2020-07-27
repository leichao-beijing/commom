package run.cmdi.common.compute.core;

import java.util.List;
import java.util.Map;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.compute.enums.LinkState;
import run.cmdi.common.compute.model.ComputeInfo;
import run.cmdi.common.compute.model.ComputeObjectInfo;
import run.cmdi.common.utils.MapIntegerObject;
import run.cmdi.common.utils.OverflowUtils;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.utils.SpotPath;
import run.cmdi.common.validator.ResultObjectInterface;

/**
 * @author leichao
 * @date 2020-05-03 11:59:06
 */
public class ResultComputeObject<T> implements ResultObjectInterface<T, ComputeInfo, ComputeObjectInfo> {
    private final JexlEngine jexlEngine;
    private final OverflowUtils<SpotPath> overflow;
    private final MapIntegerObject mapObject = new MapIntegerObject();
    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public ResultComputeObject(JexlEngine jexlEngine, OverflowUtils<SpotPath> overflow) {
        this.jexlEngine = jexlEngine;
        this.overflow = overflow;
    }

    @Override
    public void update(T t, Map<SpotPath, ComputeInfo> map) {
        map.forEach((key, value) -> {
            if (value.getComputeField() == null)
                return;
            SpotPath fieldName = value.getComputeField();
            ComputeInfo info = map.get(fieldName);
            Object computeValue = ReflectLcUtils.getObjectValue(t, fieldName.getPaths());
            if (computeValue instanceof String) {
                String val = (String) computeValue;
                value.setComputeList(ResultComputeClazz.computeMethodToList(info.getPath(), val, jexlEngine));
                if (value.getComputeList() == null)
                    throw new NullPointerException(key + "@ComputeField is null.");
                value.setState(LinkState.COMPUTE);
                overflow.add(value.getPath(), value.getComputeList());
            } else
                throw new NullPointerException(key + "@ComputeField is Object not  String type.");
        });
        map.forEach((key, value) -> {
            if (value.getComputeList() == null) {
                Object ob = ReflectLcUtils.getObjectValue(t, value.getPath().getPaths());
                if (ob == null)
                    throw new NullPointerException(value.getPath() + " is compute FieldValue can't value.");
            } else {
                List<SpotPath> list = value.getComputeList();
                for (SpotPath spotPath : list) {
                    mapObject.add(spotPath, t);
                }
                mapObject.add(value.getPath(), t);
            }
        });
    }

    @Override
    public void addInfo(T t, List<ComputeObjectInfo> list, Map<SpotPath, ComputeInfo> map) {
        map.forEach((key, value) -> {
            infoLoop(t, value, map, list);
        });
    }

    private boolean infoLoop(T t, ComputeInfo info, Map<SpotPath, ComputeInfo> map, List<ComputeObjectInfo> list) {
        if (info.getComputeValue() == null)
            return true;
        List<SpotPath> computeList = info.getComputeList();
        for (SpotPath spotPath : computeList) {
            if (!infoLoop(t, map.get(spotPath), map, list))
                return false;
        }
        Object parentObject = (info.getPath().getParent() == null) ? t
                : ReflectLcUtils.getObjectValue(t, info.getPath().getPaths());
        list.add(new ComputeObjectInfo(parentObject, info, mapObject.getMap(info.getPath().getParent())));
        return true;
    }

    @Override
    public void compute(T t, List<ComputeObjectInfo> list) {
        for (ComputeObjectInfo info : list) {
            JexlContext jc = new MapContext(info.getMapContext());
            JexlExpression je = jexlEngine.createExpression(info.getInfo().getComputeValue());
            Object value = je.evaluate(jc);
            mapObject.put(info.getInfo().getPath(), value);
            String methodName = ReflectLcUtils.methodSetString(info.getInfo().getPath().getName());
            Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(info.getInfo().getType(), methodName);
            Object oo = converterRegistry.convert(parameterClasses, value);
            if (oo != null) {
                SpotPath parent = info.getInfo().getPath().getParent();
                Object parentObject = null;
                if (parent == null)
                    parentObject = t;
                else
                    parentObject = ReflectLcUtils.getObjectValue(t, parent.getPaths());
                String valueSet = ReflectLcUtils.methodSetString(info.getInfo().getPath().getName());
                Object o1 = converterRegistry.convert(info.getInfo().getType(), oo);
                ReflectUtil.invoke(parentObject, valueSet, o1);
            }
        }
    }

}
