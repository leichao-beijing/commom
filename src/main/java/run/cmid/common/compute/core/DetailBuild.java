package run.cmid.common.compute.core;

import java.util.Map;

import org.apache.commons.jexl3.JexlEngine;

import run.cmid.common.compute.annotations.EnableCompute;
import run.cmid.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-04-13 09:29:18
 */
public class DetailBuild {
    public static ComputeDetail master(Object object, JexlEngine jexlEngine) {
        EnableCompute enableCompute = object.getClass().getAnnotation(EnableCompute.class);
        if (enableCompute == null)
            throw new NullPointerException("@EnableCompute not enable");
        return new DetailImpl(object, jexlEngine);
    }

    public static ComputeDetail sub(Object object, Object parentObject, SpotPath spotPath, String computeValue,
                                    String computeValueFieldName, JexlEngine jexlEngine, Map<String, ComputeDetail> map,
                                    Map<String, Object> mapContext) {
        return new DetailImpl(object, parentObject, spotPath, computeValue, computeValueFieldName, jexlEngine, map,
                mapContext);
    }
}
