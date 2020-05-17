package run.cmid.common.io;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.ReflectUtil;

/**
 * 
 * @author leichao
 */
public class EnumUtil {

    public static <T extends EnumTypeName> List<String> getTypeNameList(Class<T> classEnum) {
        ArrayList<String> list = new ArrayList<String>();
        for (EnumTypeName enumTypeName : values(classEnum)) {
            list.add(enumTypeName.getTypeName());
        }

        return list;
    }

    public static boolean isInterface(Class<?> classes, Class<?> interfaceClass) {
        Class<?>[] interfaces = classes.getInterfaces();
        for (Class<?> class1 : interfaces) {
            if (class1.equals(interfaceClass))
                return true;
        }
        return false;
    }

    /***
     * @param classEnum enum类
     * @param fieldName 调用fieldName进行匹配 当null时，采用默认Enum.toString
     */
    public static  List<String> getEnumNames(Class<?> classEnum, String fieldName) {
        Object[] ts = values(classEnum);
        boolean fieldState = true;
        if (fieldName.equals(""))
            fieldState = false;
        ArrayList<String> list = new ArrayList<String>();
        for (Object t : ts) {
            if (fieldState)
                list.add(ReflectUtil.invoke(t, fieldName).toString());
            else
                list.add(t.toString());
        }
        return list;
    }

    /***
     * @param classEnum enum类
     * @param typeName  搜索匹配的属性
     * @param fieldName 调用fieldName进行匹配 当null时，采用默认Enum.toString匹配
     */
    public static <T> T isEnumName(Class<T> classEnum, String typeName, String fieldName) {
        T[] ts = values(classEnum);
        boolean fieldState = true;
        if (fieldName.equals(""))
            fieldState = false;
        for (T t : ts) {
            if (fieldState) {
                if (ReflectUtil.invoke(t, fieldName).toString().equals(typeName)) {
                    return t;
                }
            } else {
                if (t.toString().equals(typeName))
                    return t;
            }
        }
        return null;
    }

    public static <T extends EnumTypeName> T isTypeName(Class<T> classEnum, String typeName) {
        T[] enums = values(classEnum);
        if (enums == null || enums.length == 0) {
            return null;
        }
        for (T t : enums) {
            if (t.getTypeName().equals(typeName)) {
                return t;
            }
        }
        return null;
    }

    public static <T> T[] values(Class<T> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Class[" + clazz + "] no Eumn");
        }
        return clazz.getEnumConstants();
    }
}
