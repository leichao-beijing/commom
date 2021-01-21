package run.cmdi.common.convert;

import cn.hutool.core.lang.Assert;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <IN>       输入数据
 * @param <REGISTER> 注册器泛型类
 */
public abstract class RegisterAbstract<IN, REGISTER extends RegisterEntity<IN>> {
    private IN inValue;

    RegisterAbstract(IN inValue) {
        Assert.notNull(inValue);
        this.inValue = inValue;
    }

    @Getter
    private final Map<String, Class<? extends REGISTER>> registerMap = new HashMap<>();

    public final void register(Class<? extends REGISTER> register) {
        REGISTER value;
        try {
            value = register.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NullPointerException(" register error " + e.getMessage());
        }
        registerMap.put(value.getRegisterName().toLowerCase(), register);
    }

    public final REGISTER getRegister(String tagName) {
        Class<? extends REGISTER> valueClazz = registerMap.get(tagName);
        if (valueClazz == null) return null;
        REGISTER value = NewInstance(valueClazz);
        if (value != null && value.isSupport(inValue))
            return value;
        return null;
    }

    public final REGISTER getRegister() {
        Iterator<Map.Entry<String, Class<? extends REGISTER>>> it = registerMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Class<? extends REGISTER>> next = it.next();
            Class<? extends REGISTER> clazz = next.getValue();
            REGISTER val = NewInstance(clazz);
            if (val != null && val.isSupport(inValue))
                return val;
        }
        return null;
    }

    private static <T> T NewInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NullPointerException(" register error " + e.getMessage());
        }
    }
}
