package run.cmdi.common.reader.annotations;

import java.lang.annotation.*;

/**
 * 查找公开方法内第一个参数annotation并进行注入
 * 只解析注解所在方法有一个对象时,进行解析.存在多个或者为null时,不进行解析
 * RegisterAnnotationInterface 对fieldName 进行注入
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterAnnotation {
    /**
     * 数值越大越最后处理
     */
    int value() default 0;
}
