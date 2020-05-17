package run.cmid.common.compute.core;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.compute.annotations.ComputeMethod;
import run.cmid.common.compute.annotations.ComputeMethodFieldName;
import run.cmid.common.compute.enums.LinkState;
import run.cmid.common.io.StringUtils;
import run.cmid.common.utils.IntegerUtils;
import run.cmid.common.utils.SpotPath;

/**
 *
 * @author leichao
 * @date 2020-04-15 01:53:33
 */
public class DetailImpl implements ComputeDetail {
    public DetailImpl(Object object, JexlEngine jexlEngine) {
        this.object = object;
        this.jexlEngine = jexlEngine;
        this.map = new HashMap<>();
        buildMap();
    }

    public DetailImpl(Object object, Object parentObject, SpotPath spotPath, String computeValue,
                      String computeValueFieldName, JexlEngine jexlEngine, Map<String, ComputeDetail> map,
                      Map<String, Object> mapContext) {
        this.object = object;
        this.parentObject = parentObject;
        this.spotPath = spotPath;
        this.jexlEngine = jexlEngine;
        this.map = map;
        this.mapContext = mapContext;
        this.computeValueFieldName = computeValueFieldName;

        try {
            this.value = IntegerUtils.ObjectToBigDecimal(object);
            this.state = LinkState.DATA;
        } catch (NumberFormatException e) {
            this.state = LinkState.NOT_COMPUTE;
        }

        setComputeValue(computeValue);
        buildMap();
    }

    @Getter
    private SpotPath spotPath;

    private final JexlEngine jexlEngine;
    @Getter
    private final Object object;
    @Getter
    private final Map<String, ComputeDetail> map;

    @Getter
    @Setter
    private LinkState state = LinkState.NOT_COMPUTE;
    @Getter
    private BigDecimal value;

    @Getter
    private String computeValue;
    @Getter
    private String computeValueFieldName;
    @Getter
    private Object parentObject;

    @Getter
    private List<String> computeValues = new ArrayList<String>();
    @Getter
    private List<String> parentNameList = new ArrayList<String>();
    @Getter
    private Map<String, Object> mapContext;

    public void setComputeValue(String computeValue) {
        if (computeValue == null)
            return;
        this.state = LinkState.COMPUTE;
        this.computeValue = computeValue;
        JexlScript script = jexlEngine.createScript(computeValue);
        Set<List<String>> variables = script.getVariables();
        for (List<String> list : variables) {
            String value = "";
            int size = list.size();
            for (int j = 0; j < size; j++) {
                if (j == 0)
                    value = list.get(j);
                else
                    value = value + StringUtils.SPOT + list.get(j);
            }
            SpotPath path = spotPath.careSomeParentSpotPath(value);
            computeValues.add(path.getPath());
        }
    }

    private void buildMap() {
        if (object == null)
            return;
        if (object.getClass().getPackageName().equals("java.lang"))
            return;
        if (mapContext == null)
            mapContext = new HashMap<String, Object>();

        Field[] fields = ReflectUtil.getFields(object.getClass());
        for (Field field : fields) {
            String computeValue = null;
            // String valuePath = null;
            SpotPath path = null;
            try {
                path = spotPath.createSub(field.getName());
            } catch (NullPointerException e) {
                path = new SpotPath(field.getName());
            }
            ComputeMethod compute = field.getAnnotation(ComputeMethod.class);
            if (compute != null)
                if (compute.value().trim().equals(""))
                    throw new NullPointerException(path.getPath() + " @Compute value can't [" + compute.value() + "]");
                else
                    computeValue = compute.value();
            String computeValueFieldName = null;
            ComputeMethodFieldName computeField = field.getAnnotation(ComputeMethodFieldName.class);
            if (computeField != null)
                if (computeField.value().trim().equals(""))
                    throw new NullPointerException(
                            path.getPath() + " @ComputeField value can't [" + computeField.value() + "]");
                else
                    computeValueFieldName = computeField.value();
            Object value = ReflectUtil.invoke(object, ("get" + StrUtil.upperFirst(field.getName())));
            ComputeDetail computeDetail = DetailBuild.sub(value, object, path, computeValue, computeValueFieldName,
                    jexlEngine, map, null);
            map.put(path.getPath(), computeDetail);
            mapContext.put(field.getName(), computeDetail.getValues());
        }
    }

    @Override
    public ComputeDetail find(String fieldName) {
        ComputeDetail value = map.get(fieldName);
        if (value == null)
            throw new NullPointerException(fieldName + " no find.");
        return value;
    }

    public boolean equals(DetailImpl obj) {
        return spotPath.equals(obj.getSpotPath());
    }

    @Override
    public void clear() {
        computeValues.clear();
        parentNameList.clear();
    }

    @Override
    public Object getValues() {
        if (value != null)
            return value;
        if (mapContext != null)
            return mapContext;
        return object;
    }

}
