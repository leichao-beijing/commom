package run.cmdi.common.register;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.register.anntications.ParameterStatic;
import run.cmdi.common.register.anntications.RegisterAnnotation;
import run.cmdi.common.utils.ReflectLcUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RegisterAnnotationUtils<T> {
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> srcClazz, Class<CONFIG> outClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return build(srcClazz, outClass, null, true);
    }

    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> srcClazz, Class<CONFIG> outClass, RegisterParameterPour pour) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return build(srcClazz, outClass, pour, true);
    }

    private RegisterParameterPour pour;

    /**
     * @param analysisClazz
     * @param configClazz
     * @param bool
     */
    public static <CONFIG, T> Map<String, CONFIG> build(Class<T> analysisClazz, Class<CONFIG> configClazz, RegisterParameterPour pour, boolean bool) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Map<String, CONFIG> result = new LinkedHashMap<>();
        List<ComputeValue> parameterList = parameterMap(configClazz);
        if(pour==null)
            pour=new RegisterParameterPour();
        if (parameterList.isEmpty())
            throw new NullPointerException("@RegisterAnnotation no find");
        for (Field field : analysisClazz.getDeclaredFields()) {
            CONFIG config = field(field, configClazz, parameterList, pour, bool);
            if (config != null) {
                result.put(field.getName(), config);
                pour.getStaticMap().forEach((key, value) -> UtilsRegister.writeObject(config, ParameterStatic.class, value));
            }
        }
        return result;
    }

    /**
     * @param field
     * @param outClass
     * @param parameterMap
     * @param bool         true时,未匹配到任何 注解的对象将被抛弃并 return null
     */
    private static <CONFIG> CONFIG field(Field field, Class<CONFIG> outClass, List<ComputeValue> parameterMap, RegisterParameterPour pour, boolean bool) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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

class UtilsRegister {
    /**
     * 通过set方式注入值
     * 将 value的值注入 object 符合 set方法中
     */
    static void writeObject(Object object, Class<? extends Annotation> annotationClass, Object value) {
        //获得 value 一致对象类的方法名称
        List<Field> fieldList = new ArrayList<Field>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass) && field.getType().equals(value.getClass()))
                fieldList.add(field);
        }
        for (Field field : fieldList) {
            String parameterValue = ReflectLcUtils.methodSetString(field.getName());
            try {
                ReflectUtil.invoke(object, parameterValue, value);
            } catch (UtilException e) {
                e.printStackTrace();
            }
        }
    }
}