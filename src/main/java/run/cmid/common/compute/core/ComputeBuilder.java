package run.cmid.common.compute.core;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;

import run.cmid.common.compute.ComputeTest;
import run.cmid.common.compute.model.ComputeInfo;
import run.cmid.common.compute.model.ComputeObjectInfo;
import run.cmid.common.utils.OverflowUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.EngineClazz;

/**
 *
 * @author leichao
 * @date 2020-05-03 11:57:31
 */
public class ComputeBuilder<T> {

    public static void main(String[] args) {
        ComputeTest ct = new ComputeTest();
        EngineClazz<ComputeTest, ComputeInfo, ResultComputeClazz, ComputeObjectInfo, ResultComputeObject<ComputeTest>> engineClazz = bulidEengineClazz(
                ComputeTest.class);
        engineClazz.engineObjcet(ct).compute();
    }

    public static <T> void compute(T t, Class<T> clazz) {
        EngineClazz<T, ComputeInfo, ResultComputeClazz, ComputeObjectInfo, ResultComputeObject<T>> engineClazz = bulidEengineClazz(
                clazz);
        engineClazz.engineObjcet(t).compute();
    }

    public static <T> EngineClazz<T, ComputeInfo, ResultComputeClazz, ComputeObjectInfo, ResultComputeObject<T>> bulidEengineClazz(
            Class<T> clazz) {
        JexlEngine jexlEngine = new JexlBuilder().create();
        OverflowUtils<SpotPath> overflow = new OverflowUtils<SpotPath>();
        ResultComputeClazz rcc = new ResultComputeClazz(jexlEngine, overflow);
        ResultComputeObject<T> rco = new ResultComputeObject<T>(jexlEngine, overflow);
        return new EngineClazz<T, ComputeInfo, ResultComputeClazz, ComputeObjectInfo, ResultComputeObject<T>>(clazz,
                rcc, rco);
    }
}
