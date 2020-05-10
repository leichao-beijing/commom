package run.cmid.common.io;

import java.util.List;

/**
 * 
 * @author leichao
 */
public class ListUtils {

    /**
     * 添加分隔符分割list数组转换成字符串类型
     */
    public static <T> String toStringMark(List<T> list, String markValue) {
        String value = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                value = list.get(i).toString();
                continue;
            }
            value = value + markValue + list.get(i).toString();
        }
        return value;
    }
}
