package run.cmdi.common.utils;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ObjectUtils {
    private static String GET = "get";
    private static String SET = "set";
    private static String IS = "is";
    private static String GET_CLASS = "getClass";

    /**
     * 对父类的克隆
     *
     * @param parentObject 克隆的父类对象
     * @param outClass     输出的子类Class对象
     * @return 子类对象实例
     */
    public static <P, OUT extends P> OUT cloneParent(P parentObject, Class<OUT> outClass) {
        OUT out = ReflectUtil.newInstance(outClass);
        return cloneParent(parentObject, out);
    }

    /**
     * 对父类的克隆
     *
     * @param parentObject 克隆的父类对象
     * @param outClass     输出的子类对象
     * @param params       创建子类对象所需要的参数
     * @return 子类对象实例
     */
    public static <P, OUT extends P> OUT cloneParent(P parentObject, Class<OUT> outClass, Object... params) {
        OUT out = ReflectUtil.newInstance(outClass, params);
        return cloneParent(parentObject, out);
    }

    private static <P, OUT extends P> OUT cloneParent(P parentObject, OUT out) {
        Method[] methods = ReflectUtil.getMethods(parentObject.getClass());
        ArrayList<String> list = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().equals(GET_CLASS))
                continue;
            if (!method.getName().startsWith(SET) && !method.getName().startsWith(IS) && !method.getName().startsWith(GET))
                continue;
            if ((method.getName().startsWith(SET) || method.getName().startsWith(IS)) && method.getParameterCount() != 1)
                continue;
            if (method.getName().startsWith(GET) && method.getParameterCount() != 0)
                continue;
            list.add(method.getName());
        }
        list.forEach((val) -> {
            String methodName;
            if (val.startsWith(GET)) {
                methodName = SET + val.substring(3);
            } else if (val.startsWith(IS)) {
                methodName = SET + val.substring(2);
            } else
                return;
            if (!list.contains(methodName))
                return;
            Object result = ReflectUtil.invoke(parentObject, val);
            if (result == null)
                return;
            ReflectUtil.invoke(out, methodName, result);
        });
        list.clear();
        return out;
    }
}
