package run.cmdi.common.register;

import java.util.HashMap;
import java.util.Map;

public class RegisterParameterPour {
    public static RegisterParameterPour build(Object... objects) {
        RegisterParameterPour registerParameterPour = new RegisterParameterPour();
        for (Object object : objects) {
            registerParameterPour.put(object);
        }
        return registerParameterPour;
    }

    private RegisterParameterPour() {
    }

    private Map<Class, Object> map = new HashMap<>();

    public Object get(Class clazz) {
        return map.get(clazz);
    }

    private void put(Object o) {
        map.put(o.getClass(), o);
    }
}
