package run.cmdi.common.register.anntications;

import java.lang.annotation.*;

/**
 * 启用该注解的对象将从外部获得参数 必须存在set方法
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterStatic {
    /**
     * 默认抛 NullPointerException 异常
     */
    boolean value() default true;

    String alias() default "";
}