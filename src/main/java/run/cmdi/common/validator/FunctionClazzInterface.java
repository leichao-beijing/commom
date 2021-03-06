package run.cmdi.common.validator;

import run.cmdi.common.utils.SpotPath;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author leichao
 * @date 2020-05-01 02:57:02
 */
public interface FunctionClazzInterface<T> {
    /**
     * @return null时，不写入数据
     */
    T resultField(SpotPath path, Field field);


    /**
     * 对数据进行校验，存在问题时抛出异常
     */
    void validator(Map<SpotPath, T> fieldMap);


}
