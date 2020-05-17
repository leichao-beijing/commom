package run.cmid.common.compute.core;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;

import run.cmid.common.compute.ComputeTest;
import run.cmid.common.compute.model.ComputeInfo;
import run.cmid.common.utils.OverflowUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.EngineClazz;

/**
 *
 * @author leichao
 * @date 2020-05-03 11:57:31
 */
public class ComputeBuilder<T> {
    private final JexlEngine jexlEngine = new JexlBuilder().create();
    private final OverflowUtils<SpotPath> overflow = new OverflowUtils<SpotPath>();
    private ResultComputeObject<T> rco;
    private final EngineClazz<T, ComputeInfo, ResultComputeClazz> engineClazz;

    private ComputeBuilder(Class<T> clazz) {
        ResultComputeClazz rcc = new ResultComputeClazz(jexlEngine, overflow);
        this.engineClazz = new EngineClazz<T, ComputeInfo, ResultComputeClazz>(clazz, rcc);
    }

    public void compute(T t) {
        if (rco == null)
            rco = new ResultComputeObject<T>(jexlEngine, overflow);
        engineClazz.engineObject(t, rco).compute();
    }

    public static void main(String[] args) {
        ComputeTest ct = new ComputeTest();
        ComputeBuilder.build(ComputeTest.class).compute(ct);
    }

    public static <T> ComputeBuilder<T> build(Class<T> clazz) {
        return new ComputeBuilder<T>(clazz);
    }
}
