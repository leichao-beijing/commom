package run.cmid.common.validator;

import run.cmid.common.compute.ComputeTest;

/**
 *
 * @author leichao
 * @date 2020-05-03 11:53:36
 */
public class ValidatorBuilderExample {

    public static <T> EngineClazz<T, String, FunctionDefaultClazz> build(Class<T> clazz) {
        return new EngineClazz<T, String, FunctionDefaultClazz>(clazz, new FunctionDefaultClazz());
    }

    public static void main(String[] args) {
        ComputeTest cc = new ComputeTest();
        EngineClazz<ComputeTest, String, FunctionDefaultClazz> data = ValidatorBuilderExample.build(ComputeTest.class);
        EngineObject<ComputeTest, String, String, FunctionDefaultObject<ComputeTest>> engineObject = data
                .engineObject(cc, new FunctionDefaultObject<ComputeTest>());
        engineObject.compute();
    }
}
