package run.cmdi.common.convert;

import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.reader.annotations.RegisterAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, CONFIG> result = new HashMap<>();
        Map<Method, Class> parameterMap = parameterMap(configClazz);
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
    private static <CONFIG> CONFIG field(Field field, Class<CONFIG> outClass, Map<Method, Class> parameterMap, boolean bool) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        CONFIG config = outClass.getDeclaredConstructor().newInstance();
        boolean state = false;
        for (Map.Entry<Method, Class> stringClassEntry : parameterMap.entrySet()) {
            Class value = stringClassEntry.getValue();
            Method method = stringClassEntry.getKey();
            if (config instanceof RegisterAnnotationInterface)
                ((RegisterAnnotationInterface) config).setFieldName(field.getName());
            if (value.isAnnotation()) {
                Annotation annotation = field.getAnnotation(value);
                if (annotation == null)
                    continue;
                ReflectUtil.invoke(config, method, annotation);
            } else if (value.isAssignableFrom(Field.class)) {
                ReflectUtil.invoke(config, method, field);
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
    private static <CONFIG> Map<Method, Class> parameterMap(Class<CONFIG> outClass) {
        Method[] methods = outClass.getDeclaredMethods();
        Map<Method, Class> parameters = new HashMap<>();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RegisterAnnotation.class))
                continue;
            if (method.getParameterCount() != 1)
                continue;
            parameters.put(method, method.getParameterTypes()[0]);
        }
        return parameters;
    }

}
