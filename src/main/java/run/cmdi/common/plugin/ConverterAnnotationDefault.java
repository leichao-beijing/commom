package run.cmdi.common.plugin;

import java.lang.annotation.Annotation;

public interface ConverterAnnotationDefault<T extends Annotation> extends Annotation {

    default String getDescribe() {
        return getClass().getName();
    }

    /**
     * 具有唯一性的name
     */
    default String getKeyName() {
        return getClass().getName();
    }


    /**
     * 通过 注解T 对对象内数据进行初始化操作
     */
    void initialize(T t);

    default ConverterAnnotationDefault<T> buildAnnotation() {
        return this;
    }

    /**
     * 返回第一个注解接口的class对象
     */
    default Class<? extends Annotation> annotationType() {
        for (Class<?> anInterface : getClass().getInterfaces()) {
            if (anInterface.isAnnotation()) {
                return (Class<? extends Annotation>) anInterface;
            }
        }
        return getClass();
    }
}
