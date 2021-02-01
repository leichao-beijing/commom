package run.cmdi.common.convert;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.RegisterAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class RegisterAnnotationUtils<T> {
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> srcClazz, Class<CONFIG> outClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return build(srcClazz, outClass, true);
    }

    /**
     * @param analysisClazz
     * @param configClazz
     * @param bool          true时,未匹配到任何 注解的对象将被抛弃并return null
     */
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> analysisClazz, Class<CONFIG> configClazz, boolean bool) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Map<String, CONFIG> result = new LinkedHashMap<>();
        Map<ComputeValue<Method>, Class> parameterMap = parameterMap(configClazz);
        if (parameterMap.isEmpty())
            throw new NullPointerException("@RegisterAnnotation no find");
        for (Field field : analysisClazz.getDeclaredFields()) {
            CONFIG config = field(field, configClazz, parameterMap, bool);
            if (config != null)
                result.put(field.getName(), config);
        }
        return result;
    }

    /**
     * @param field
     * @param outClass
     * @param parameterMap
     * @param bool         true时,未匹配到任何 注解的对象将被抛弃并return null
     */
    private static <CONFIG> CONFIG field(Field field, Class<CONFIG> outClass, Map<ComputeValue<Method>, Class> parameterMap, boolean bool) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        CONFIG config = outClass.getDeclaredConstructor().newInstance();
        boolean state = false;
        for (Map.Entry<ComputeValue<Method>, Class> stringClassEntry : parameterMap.entrySet()) {
            Class value = stringClassEntry.getValue();
            ComputeValue<Method> method = stringClassEntry.getKey();
            if (config instanceof RegisterAnnotationInterface)
                ((RegisterAnnotationInterface) config).setFieldName(field.getName());
            if (value.isAnnotation()) {
                Annotation annotation = field.getAnnotation(value);
                if (annotation == null)
                    continue;
                ReflectUtil.invoke(config, method.get(), annotation);
            } else if (value.isAssignableFrom(Field.class)) {
                ReflectUtil.invoke(config, method.get(), field);
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
    private static <CONFIG> Map<ComputeValue<Method>, Class> parameterMap(Class<CONFIG> outClass) {
        Method[] methods = outClass.getDeclaredMethods();
        Map<ComputeValue<Method>, Class> parameters = new TreeMap<>();
        for (Method method : methods) {
            RegisterAnnotation registerAnnotation = method.getAnnotation(RegisterAnnotation.class);
            if (registerAnnotation == null)
                continue;
            if (method.getParameterCount() != 1)
                continue;
            parameters.put(new ComputeValue(registerAnnotation.value(), method), method.getParameterTypes()[0]);
        }
        return parameters;
    }


}

@Getter
@Setter
class ComputeValue<T> implements Comparable<ComputeValue> {
    public ComputeValue(int index, Method method) {
        this.index = index;
        this.method = method;

    }

    public Method get() {
        return method;
    }

    private final int index;
    private final Method method;

    @Override
    public int compareTo(ComputeValue computeValue) {
        return index - computeValue.index;
    }
}
