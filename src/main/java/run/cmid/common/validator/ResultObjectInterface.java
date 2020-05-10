package run.cmid.common.validator;

import java.util.List;
import java.util.Map;

import run.cmid.common.utils.SpotPath;

/**
 *
 * @author leichao
 * @date 2020-05-02 10:58:55
 */
public interface ResultObjectInterface<T, RETURN1, RETURN2> {
    /**
     * @return null时，不写入数据
     * @param path   对象位置
     * @param field
     * @param object 对象的值
     */
    // RETURN2 info(T t, SpostPath path, RETURN1 return1, Map<SpostPath, RETURN1>
    // map);

    /**
     * 用于更新 内RETURN1数据
     * 
     * @param t
     * @param map
     */
    void update(T t, Map<SpotPath, RETURN1> map);

    /**
     * 增加 List<RETURN2> 数据
     * 
     * @param list 增加的数据
     * @param map
     */
    void addInfo(T t, List<RETURN2> list, Map<SpotPath, RETURN1> map);

    void compute(T t,List<RETURN2> list);
}
