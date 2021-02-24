package run.cmdi.common.register;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.register.anntications.RegisterAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RegisterAnnotationUtils<T> {
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> srcClazz, Class<CONFIG> outClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return build(srcClazz, outClass, true);
    }

    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> srcClazz, Class<CONFIG> outClass, RegisterParameterPour pour) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return build(srcClazz, outClass, true);
    }

    private RegisterAnnotationUtils into(RegisterParameterPour pour) {
        this.pour = pour;
        return this;
    }

    private RegisterParameterPour pour;

    /**
     * @param analysisClazz
     * @param configClazz
     * @param bool
     */
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> analysisClazz, Class<CONFIG> configClazz, boolean bool, RegisterParameterPour pour) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Map<String, CONFIG> result = new LinkedHashMap<>();
        List<ComputeValue> parameterList = parameterMap(configClazz);
        if (parameterList.isEmpty())
            throw new NullPointerException("@RegisterAnnotation no find");
        for (Field field : analysisClazz.getDeclaredFields()) {
            CONFIG config = field(field, configClazz, parameterList, bool);
            if (config != null)
                result.put(field.getName(), config);
        }
        return result;
    }

    /**
     * @param field
     * @param outClass
     * @param parameterMap
     * @param bool         true时,未匹配到任何 注解的对象将被抛弃并 return null
     */
    private static <CONFIG> CONFIG field(Field field, Class<CONFIG> outClass, List<ComputeValue> parameterMap, boolean bool) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        CONFIG config = outClass.getDeclaredConstructor().newInstance();
        boolean state = false;

        if (config instanceof RegisterAnnotationInterface)
            ((RegisterAnnotationInterface) config).setFieldName(field.getName());

        for (ComputeValue computeValue : parameterMap) {
            if (computeValue.getClazz().isAnnotation()) {
                Annotation annotation = field.getAnnotation(computeValue.getClazz());
                if (annotation == null)
                    continue;
                ReflectUtil.invoke(config, computeValue.getMethod(), annotation);
            } else if (computeValue.getClazz().isAssignableFrom(Field.class)) {
                ReflectUtil.invoke(config, computeValue.getMethod(), field);
                continue;
            } else continue;
            state = true;
        }
        if (!state && bool) {
            return null;
        }
        return config;
    }

    /**
     * 将带有@RegisterAnnotation 注解的 method 装入map中 并将所对应的参数进行收集
     */
    private static <CONFIG> List<ComputeValue> parameterMap(Class<CONFIG> outClass) {
        Method[] methods = outClass.getDeclaredMethods();
        List<ComputeValue> parameters = new ArrayList<>();
        for (Method method : methods) {
            RegisterAnnotation registerAnnotation = method.getAnnotation(RegisterAnnotation.class);
            if (registerAnnotation == null)
                continue;
            if (method.getParameterCount() != 1)
                continue;
            ComputeValue value = new ComputeValue(registerAnnotation.value(), method, method.getParameterTypes()[0]);
            parameters.add(value);
        }
        parameters.sort(new Comparator<ComputeValue>() {
            @Override
            public int compare(ComputeValue o1, ComputeValue o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
        return parameters;
    }
}

@Getter
@Setter
class ComputeValue {
    public ComputeValue(int index, Method method, Class clazz) {
        this.index = index;
        this.clazz = clazz;
        this.method = method;
    }

    private final Class clazz;
    private final int index;
    private final Method method;

}
