package run.cmdi.common.plugin;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterTable<T> {
    private Class<T> clazz;
    private Map<Class<Annotation>, Class<? extends ConverterAnnotation>> map;

    public RegisterTable(Class<T> clazz, Map<Class<Annotation>, Class<? extends ConverterAnnotation>> map) {
        this.clazz = clazz;
        this.map = map;
    }

    Map<Class<? extends ConverterAnnotation>, Class<Annotation>> convertMap = new HashMap<>();

    private void ss(Annotation annotation) {
        Method[] methods = ReflectUtil.getMethods(clazz);
        T instance = ReflectUtil.newInstanceIfPossible(clazz);
        for (Method method : methods) {
            Class[] types = method.getParameterTypes();
            if (types.length != 1) continue;
            if (!types[0].getSuperclass().equals(ConverterAnnotation.class)) continue;
            ConverterAnnotation convert = (ConverterAnnotation) ReflectUtil.newInstanceIfPossible(types[0]);
            convert.initialize(annotation);
            ReflectUtil.invoke(instance, method.getName(), convert);
        }
    }

    public PutClass put(Annotation annotation) {

        PutClass put = new PutClass();


        return null;
    }

}

class PutClass {
    PutClass(Map<Class<? extends ConverterAnnotation>, Class<Annotation>> convertMap) {
        this.convertMap = convertMap;
    }

    @Getter
    private final Map<Class<? extends ConverterAnnotation>, Class<Annotation>> convertMap;

    public void put(Annotation annotation) {

    }
}

@Getter
@Setter
class Info {
    Class<Annotation> annotationClass;
    String name;
}