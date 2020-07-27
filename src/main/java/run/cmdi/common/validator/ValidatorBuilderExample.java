package run.cmdi.common.validator;

/**
 * @author leichao
 * @date 2020-05-03 11:53:36
 */
public class ValidatorBuilderExample {

    public static <T> EngineClazz<T, String, FunctionDefaultClazz> build(Class<T> clazz) {
        return new EngineClazz<T, String, FunctionDefaultClazz>(clazz, new FunctionDefaultClazz());
    }

}
