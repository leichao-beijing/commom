package run.cmdi.common.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import run.cmdi.common.utils.OverflowUtils;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-05-01 02:52:02
 */
public class EngineClazz<T, RETURN1, FUN1 extends FunctionClazzInterface<RETURN1>>
        implements FindSpotPath<RETURN1> {
    private final OverflowUtils<SpotPath> overflow;
    @Getter
    private final Map<SpotPath, RETURN1> fieldMap = new HashedMap<SpotPath, RETURN1>();
    private boolean loopState;
    private final FUN1 funClazz;

    public static <RETURN1> Map<String, RETURN1> getStringMap(Map<SpotPath, RETURN1> fieldMap) {
        HashMap<String, RETURN1> map = new HashMap<String, RETURN1>();
        fieldMap.forEach((key, val) -> {
            map.put(key.getPath(), val);
        });
        return map;
    }

    public static <RETURN1> Map<SpotPath, RETURN1> getSpotPathMap(Map<String, RETURN1> fieldMap) {
        HashMap<SpotPath, RETURN1> map = new HashMap<SpotPath, RETURN1>();
        fieldMap.forEach((key, val) -> {
            map.put(new SpotPath(key), val);
        });
        return map;
    }

    /**
     * @param clazz     需要分析的class类
     * @param loopState 是否递归解析下位class对象 不进行递归分析
     */
    public EngineClazz(Class<T> clazz, FUN1 funClazz, boolean loopState) {
        this.loopState = loopState;
        overflow = new OverflowUtils<SpotPath>();
        this.funClazz = funClazz;
        analysis(clazz);
    }

    public EngineClazz(Class<T> clazz, FUN1 funClazz) {
        this(clazz, funClazz, false);
    }

    private void analysis(Class<T> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            SpotPath path = new SpotPath(field.getName());
            if (!ReflectLcUtils.isJavaClass(field.getType()) && !field.getType().isEnum()) {
                if (!loopState)
                    continue;
                analysisLoop(path, field.getType());
                continue;
            }
            RETURN1 re = funClazz.resultField(path, field);
            if (re != null)
                fieldMap.put(path, re);
        }
        funClazz.validator(fieldMap);
    }

    private void analysisLoop(SpotPath parent, Class<?> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        ArrayList<SpotPath> paths = new ArrayList<SpotPath>();
        for (Field field : fields) {
            SpotPath path = parent.createSub(field.getName());
            if (!ReflectLcUtils.isJavaClass(field.getType()))
                analysisLoop(path, field.getType());
            else {
                RETURN1 re = funClazz.resultField(path, field);
                if (re != null)
                    fieldMap.put(path, re);
            }
        }
        if (paths.size() != 0)
            overflow.add(parent, paths);
    }

    @Override
    public RETURN1 find(SpotPath path) {
        return fieldMap.get(path);
    }

    public <RETURN2, FUN2 extends ResultObjectInterface<T, RETURN1, RETURN2>> EngineObject<T, RETURN1, RETURN2, FUN2> engineObject(T t, FUN2 funObject) {
        return new EngineObject<T, RETURN1, RETURN2, FUN2>(t, fieldMap, funObject);
    }

}
