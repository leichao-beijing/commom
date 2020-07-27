package run.cmdi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * @author leichao
 * @date 2020-05-06 09:43:08
 */
public class MapIntegerObject {
    @Getter
    private Map<String, Object> map = new HashMap<String, Object>();
    private List<SpotPath> paths = new ArrayList<SpotPath>();
    ;

    /**
     * 已重复的path无法再次添加
     *
     * @param path
     * @param value
     * @throws ClassCastException
     */
    public void add(SpotPath path, Object value) {
        if (!paths.contains(path))
            add(path, value, false, false);
    }

    /**
     * @param path
     * @param value
     * @throws ClassCastException
     */
    public void put(SpotPath path, Object value) {
        add(path, value, true, true);
    }

    /**
     * @param path
     * @param castState true时ClassCastException存在时，将会抛出异常。
     * @param state     true时直接存在数据，false时采用ReflectLcUtils.getObjectValue
     *                  获取对应pathValue值
     * @throws ClassCastException
     */
    @SuppressWarnings("unchecked")
    public void add(SpotPath path, Object value, boolean castState, boolean state) {
        String[] paths = path.getPaths();
        Map<String, Object> tmp = map;
        Object obj = null;
        for (int i = 0; i < paths.length; i++) {
            if (i == paths.length - 1) {
                if (!state)
                    tmp.put(paths[i],
                            IntegerUtils.ObjectToBigDecimal(ReflectLcUtils.getObjectValue(value, path.getPaths())));
                else
                    tmp.put(paths[i], IntegerUtils.ObjectToBigDecimal(value));
                continue;
            }
            obj = tmp.get(paths[i]);
            if (obj == null) {
                HashMap<String, Object> mm = new HashMap<String, Object>();
                tmp.put(paths[i], mm);
                tmp = mm;
            } else {
                if (obj instanceof Map) {
                    tmp = (HashMap<String, Object>) obj;
                } else {
                    if (castState)
                        throw new ClassCastException(path + " Override");
                    else {
                        HashMap<String, Object> mm = new HashMap<String, Object>();
                        tmp.put(paths[i], mm);
                        tmp = mm;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(SpotPath parent) {
        if (parent == null)
            return map;
        String[] paths = parent.getPaths();
        Map<String, Object> tmp = map;
        for (int i = 0; i < paths.length; i++) {
            Object obj = tmp.get(paths[i]);
            if (i == paths.length - 1)
                if (obj instanceof Map)
                    return (HashMap<String, Object>) obj;
                else
                    return null;
            tmp = (HashMap<String, Object>) obj;
        }
        return null;
    }
}
