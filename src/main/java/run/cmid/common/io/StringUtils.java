package run.cmid.common.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StringUtils {
    public static final String SPOT = ".";
    public static final String SPOT_SPLIT = "\\.";

    public static <T> List<T> copyList(List<T> list) {
        List<T> newList = new ArrayList<T>();
        for (T t : list) {
            newList.add(t);
        }
        return newList;
    }

    /**
     * 获取指定字符串的位置的前半部分。
     */
    public static String getValueHead(String value, String tag) {
        Optional<String> nameHead = Optional.ofNullable(value).map(a -> {
            if (a.lastIndexOf(tag) == -1)
                return a;
            return a.substring(0, a.lastIndexOf(tag));
        });
        if (nameHead.isEmpty())
            return "";
        return nameHead.get();
    }

    /**
     * 获取指定字符串的后半部分
     *
     * @param value 需要截取的字符串
     * @param tag   标记值
     */
    public static String getTagAfterValue(String value, String tag) {
        Optional<String> nameBody = Optional.ofNullable(value).map(a -> {
            if (a.lastIndexOf(tag) == -1)
                return "";
            return a.substring(a.lastIndexOf(tag) + tag.length());
        });
        if (nameBody.isEmpty())
            return "";
        return nameBody.get();
    }

    /**
     * 获取指定字符串的前半部分 没有找到tag时返回"";
     *
     * @param value 需要截取的字符串
     * @param tag   标记值
     */
    public static String getTagBeforeValue(String value, String tag) {
        Optional<String> nameBody = Optional.ofNullable(value).map(a -> {
            if (a.lastIndexOf(tag) == -1)
                return "";
            return a.substring(0, a.lastIndexOf(tag));
        });
        if (nameBody.isEmpty())
            return "";
        return nameBody.get();
    }

    /**
     * 获扩展名
     */
    public static String getValueSuffix(String value) {
        return getTagAfterValue(value, SPOT);
    }

    /**
     * 获扩名称不包括扩展名
     */
    public static String getHeadString(String value) {
        return getTagBeforeValue(value, SPOT);
    }

    /**
     * Turn a byte of between -127 and 127 into something between 0 and 255, so
     * distance calculations work as expected.
     */
    public static int unsignedInt(byte b) {
        return 0xFF & b;
    }

    /**
     * 验证字符串是否为空。不计算左右空格
     */
    public static boolean isEmpty(final String target) {
        if (target == null || target.length() == 0)
            return true;
        return target.trim().length() == 0;
    }

    /**
     * 验证对象是否为空，或者toString().length()=0
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null || obj.toString().length() == 0)
            return true;
        return false;
    }

    /**
     * 取value指定findValue字符串后面的第address个内容
     */
    public static String getString(String value, String findValue, int address) {
        int i = value.indexOf(findValue) + findValue.length();
        return value.substring(i, i + address);
    }

    public static boolean isEmptyExtend(String value) {
        if (value == null || value.equals("") || value.equals("-1"))
            return true;
        return false;
    }

}
