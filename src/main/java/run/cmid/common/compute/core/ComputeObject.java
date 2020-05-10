package run.cmid.common.compute.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;

/**
 *
 * @author leichao
 * @date 2020-04-13 09:08:10
 */
public class ComputeObject<T> {
    private ComputeObject() {
    }

    public static <T> void compute(T t) {
        LinkedHashMap<String, ComputeDetail> runMap = new LinkedHashMap<String, ComputeDetail>();
        JexlEngine jexlEngine = new JexlBuilder().create();
        ComputeDetail fieldDetail = DetailBuild.master(t, jexlEngine);
        validator(fieldDetail.getMap(), runMap);
        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
        runMap.forEach((a, c) -> {
            Map<String, Object> tmp = getMap(fieldDetail.getMapContext(), c.getSpotPath());
            JexlContext jc = new MapContext(tmp);
            JexlExpression je = jexlEngine.createExpression(c.getComputeValue());
            Object value = je.evaluate(jc);
            tmp.put(c.getSpotPath().getName(), value);
            String methodName = ReflectLcUtils.methodSetString(c.getSpotPath().getName());
            Class<?> parameterClasses = ReflectLcUtils.getMethodParameterTypeFirst(c.getParentObject().getClass(),
                    methodName);
            Object oo = converterRegistry.convert(parameterClasses, value);
            if (oo != null)
                ReflectUtil.invoke(c.getParentObject(), ReflectLcUtils.methodSetString(c.getSpotPath().getName()), oo);

        });
    }

    /**
     * 对ComputeField Compute 注解进行校验，出现loop等错误抛出异常
     */
    public static void validator(Map<String, ComputeDetail> map, LinkedHashMap<String, ComputeDetail> runMap) {
        FieldValidator.field(map);
        FieldValidator.loop(map, runMap);
        FieldValidator.clear(map);
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> map, SpotPath path) {
        String[] values = path.getPaths();
        Map<String, Object> tmp = null;
        for (int i = 0; i < values.length; i++) {
            if (values.length == 1) {
                tmp = map;
                break;
            }
            if (values.length == i + 1)
                break;
            Object value = map.get(values[i]);
            tmp = (Map<String, Object>) value;
        }
        return tmp;
    }
}
