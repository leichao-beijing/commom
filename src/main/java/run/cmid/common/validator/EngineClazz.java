package run.cmid.common.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import run.cmid.common.utils.OverflowUtils;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;

/**
 *
 *
 * @author leichao
 * @date 2020-05-01 02:52:02
 */
public class EngineClazz<T, RETURN1, FUN1 extends FunctionClazzInterface<RETURN1>,RETURN2, FUN2 extends ResultObjectInterface<T, RETURN1,RETURN2>>
        implements FindSpotPath<RETURN1> {
    private final OverflowUtils<SpotPath> overflow;
    @Getter
    private final Map<SpotPath, RETURN1> fieldMap = new HashedMap<SpotPath, RETURN1>();
    private boolean loopState = true;
    private final FUN1 funClazz;
    private final FUN2 funObject;

    /**
     * @param clazz     需要分析的class类
     * @param loopState 是否递归解析下位class对象 默认进行递归
     */
    public EngineClazz(Class<T> clazz, FUN1 funClazz, FUN2 funObject, boolean loopState) {
        this.loopState = loopState;
        overflow = new OverflowUtils<SpotPath>();
        this.funClazz = funClazz;
        this.funObject = funObject;
        analysis(clazz);
    }

    public EngineClazz(Class<T> clazz, FUN1 funClazz, FUN2 funObject) {
        this(clazz, funClazz, funObject, true);
    }

    private void analysis(Class<T> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            SpotPath path = new SpotPath(field.getName());
            if (!ReflectLcUtils.isJavaClass(field.getType())) {
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

    public EngineObject<T, RETURN1,RETURN2, FUN2> engineObjcet(T t) {
        return new EngineObject<T, RETURN1,RETURN2, FUN2>(t, fieldMap, funObject);
    }

}
