package run.cmid.common.validator;

import java.lang.reflect.Field;
import java.util.Map;

import run.cmid.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-05-02 09:22:41
 */
public class FunctionDefaultClazz implements FunctionClazzInterface<String> {

    @Override
    public String resultField(SpotPath path, Field field) {
        return path.toString();
    }

    @Override
    public void validator(Map<SpotPath, String> fieldMap) {
        // TODO Auto-generated method stub

    }

}
