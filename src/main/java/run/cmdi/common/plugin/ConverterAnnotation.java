package run.cmdi.common.plugin;

import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ConverterAnnotation<T extends Annotation> implements ConverterAnnotationDefault<T> {


    public final static <A extends Annotation, T extends ConverterAnnotation<A>> List<T> build(Supplier<T> fun, A[] a) {
        List<T> list = new ArrayList<T>();
        for (A a1 : a) {
            T t = fun.get();
            t.initialize(a1);
            list.add(t);
        }
        return list;
    }

    /**
     * 通过 field对ConverterAnnotation对象进行初始化操作
     *
     * @return false 未找到对应注解类
     */
    public final static <T extends ConverterAnnotation> T instance(Field field, Class<T> clazz) {
        T value = ReflectUtil.newInstanceIfPossible(clazz);
        Annotation annotation = field.getAnnotation(value.annotationType());
        if (annotation == null)
            return null;
        value.initialize(annotation);
        return value;
    }
}
