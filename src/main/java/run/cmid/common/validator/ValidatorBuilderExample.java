package run.cmid.common.validator;

import run.cmid.common.compute.ComputeTest;

/**
 * @author leichao
 * @date 2020-05-03 11:53:36
 */
public class ValidatorBuilderExample {

    public static <T> EngineClazz<T, String, FunctionDefaultClazz> build(Class<T> clazz) {
        return new EngineClazz<T, String, FunctionDefaultClazz>(clazz, new FunctionDefaultClazz());
    }

}
