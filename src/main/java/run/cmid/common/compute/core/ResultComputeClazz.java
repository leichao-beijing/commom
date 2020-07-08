package run.cmid.common.compute.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;

import run.cmid.common.compute.annotations.ComputeMethod;
import run.cmid.common.compute.annotations.ComputeMethodFieldName;
import run.cmid.common.compute.model.ComputeInfo;
import run.cmid.common.io.StringUtils;
import run.cmid.common.utils.OverflowUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.FunctionClazzInterface;

/**
 * @author leichao
 * @date 2020-05-03 11:59:06
 */
public class ResultComputeClazz implements FunctionClazzInterface<ComputeInfo> {
    private final JexlEngine jexlEngine;
    private final OverflowUtils<SpotPath> overflow;

    public ResultComputeClazz(JexlEngine jexlEngine, OverflowUtils<SpotPath> overflow) {
        this.jexlEngine = jexlEngine;
        this.overflow = overflow;
    }

    @Override
    public ComputeInfo resultField(SpotPath path, Field field) {
        ComputeMethod computeMethod = field.getAnnotation(ComputeMethod.class);
        List<SpotPath> list = null;
        ComputeMethodFieldName computeMethodFieldName = field.getAnnotation(ComputeMethodFieldName.class);
        if (computeMethod != null && computeMethodFieldName != null) {
            throw new NullPointerException("ComputeMethod and ComputeMethodFieldName Override");
        }
        if (computeMethod != null) {
            list = computeMethodToList(path, computeMethod.value(), jexlEngine);
            if (list != null)
                overflow.add(path, list);
        }
        if (computeMethodFieldName != null)
            if (computeMethodFieldName.value().trim().equals(""))
                throw new NullPointerException("computeMethodFieldName.value is null");
            else
                return new ComputeInfo(path, path.careSomeParentSpotPath(computeMethodFieldName.value()),
                        field.getType());
        return new ComputeInfo(path, (computeMethod == null) ? null : computeMethod.value(), list, field.getType());
    }

    public static List<SpotPath> computeMethodToList(SpotPath path, String computeValue,
                                                     JexlEngine jexlEngine) {
        JexlScript script = jexlEngine.createScript(computeValue);
        Set<List<String>> variables = script.getVariables();
        List<SpotPath> paths = new ArrayList<SpotPath>();
        for (List<String> list : variables) {
            String value = "";
            int size = list.size();
            for (int j = 0; j < size; j++) {
                if (j == 0)
                    value = list.get(j);
                else
                    value = value + StringUtils.SPOT + list.get(j);
            }
            SpotPath pathList = path.careSomeParentSpotPath(value);
            if (!paths.contains(pathList))
                paths.add(pathList);
        }
        if (paths.size() != 0)
            return paths;
        return null;
    }

    @Override
    public void validator(Map<SpotPath, ComputeInfo> fieldMap) {
        fieldMap.forEach((key, value) -> {
            SpotPath path = value.getComputeField();
            if (path != null) {
                ComputeInfo info = fieldMap.get(path);
                if (info == null)
                    throw new NullPointerException(key + " @ComputeField.value no find.");
                if (!info.getType().equals(String.class))
                    throw new NullPointerException(key + " @ComputeField.value is field class type not support.");
            }
        });
    }
}
