package run.cmdi.common.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.io.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author leichao
 */
public class ReflectLcUtils {
    public static <T extends Annotation> List<Field> getAnnotationInField(Class<?> classes, Class<T> annotationClass) {
        Field[] fields = classes.getDeclaredFields();
        ArrayList<Field> list = new ArrayList<Field>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                list.add(field);
            }
        }
        return list;
    }
    /**
     * 将 value 首字母大写并在前面添加get字符串
     * */
    public static String methodGetString(String value) {
        return "get" + ReflectLcUtils.upperCase(value);
    }

    /**
     * 将 value 首字母大写并在前面添加set字符串
     * */
    public static String methodSetString(String value) {
        return "set" + ReflectLcUtils.upperCase(value);
    }

    /**
     * 验证srcClass内的methodName方法中的第一参数是否与validClass一致
     */
    public static Boolean isParameterTypeOne(Class<?> srcClass, String methodName, Class<?> validClass) {
        Optional<String> strOpt = getMethodParameterType(srcClass, methodName);
        if (strOpt.isEmpty())
            return false;
        if (strOpt.get().equals(validClass.getName()))
            return true;
        return false;
    }

    /**
     * 获取对象方法内传入参数的属性
     */
    public static Class<?> getMethodParameterTypeFirst(Class<?> srcClass, String methodName) {
        Method[] methods = ReflectUtil.getMethods(srcClass);
        if (ArrayUtil.isNotEmpty(methods)) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method.getParameterTypes()[0];
                }
            }
        }
        return null;
    }

    /**
     * 通过反射获得srcClass内对应methodName方法的属性
     */
    public static Optional<String> getMethodParameterType(Class<?> srcClass, String methodName) {
        Method[] methods = ReflectUtil.getMethods(srcClass);
        if (ArrayUtil.isNotEmpty(methods)) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return Optional.ofNullable(method.getParameterTypes()).map(a -> a[0].getName());
                }
            }
        }
        return Optional.ofNullable(null);
    }

    /**
     * 首字母大写
     */
    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 验证的class是否为java的原生类
     *
     * @param clazz 验证的class类
     */
    public static boolean isJavaClass(Class<?> clazz) {
        String[] values = clazz.getTypeName().split(StringUtils.SPOT_SPLIT);
        if (values.length == 1)
            return false;
        return values[0].equals("java");
    }

    public static Object getObjectValue(Object ob, String... values) {
        Object tmp = ob;
        for (int i = 0; i < values.length; i++) {
            String valueGet = ReflectLcUtils.methodGetString(values[i]);
            ob = ReflectUtil.invoke(ob, valueGet);
            if (ob == null) {
                if (i == values.length - 1)
                    return null;
                throw new NullPointerException(tmp.getClass() + StringUtils.SPOT + valueGet + " no find.");
            }
            tmp = ob;
        }
        return ob;
    }
    /**
     * 验证classes 中是否存在interfaceClasses接口类
     */
    public static boolean IsInterface(Class<?> classes, Class<?> interfaceClasses) {
        Class<?>[] is = classes.getInterfaces();
        if (classes.equals(interfaceClasses)) {
            return true;
        }
        for (Class<?> i : is) {
            if (i.equals(interfaceClasses))
                return true;
        }
        return false;
    }
//    public static <T extends Annotation> void toAnnotationMap(Field field) {
//        Map<Class<? extends T>, Annotation> map = new HashMap<>();
//        Annotation[] annotations = field.getAnnotations();
//        for (Annotation annotation : annotations) {
//            Class<? extends Annotation> ss = annotation.getClass();
//            map.put(ss,annotation);
//        }
//    }
}
