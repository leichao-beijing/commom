package run.cmdi.common.register;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class RegisterParameterPour {
    /**
     * 注册静态参数,在这个配置生命周期中,长期有效
     */
    public RegisterParameterPour registerStatic(Object... objects) {
        RegisterParameterPour registerParameterPour = new RegisterParameterPour();
        for (Object object : objects) {
            staticMap.put(object.getClass(), object);
        }
        return this;
    }

    /**
     * 注册动态参数,每次创建一个新的对象时,需要重新传值
     */
    public RegisterParameterPour registerDynamic(Class... objects) {
        for (Class object : objects) {
            dynamicMap.put(object, object);
        }
        return this;
    }

    @Getter
    private final Map<Class, Object> staticMap = new HashMap<>();

    @Getter
    private final Map<Class, Class> dynamicMap = new HashMap<>();
}
