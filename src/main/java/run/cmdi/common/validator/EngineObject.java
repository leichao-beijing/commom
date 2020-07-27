package run.cmdi.common.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import run.cmdi.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-05-02 10:51:49
 */
public class EngineObject<T, RETURN1, RETURN2, FUN2 extends ResultObjectInterface<T, RETURN1, RETURN2>> {
    private final Map<SpotPath, RETURN1> map;
    private final T t;
    private FUN2 fun2;
    private final List<RETURN2> list;

    public EngineObject(T t, Map<SpotPath, RETURN1> map, FUN2 fun2) {
        this.map = map;
        this.t = t;
        this.fun2 = fun2;
        this.list = new ArrayList<RETURN2>();
    }

    public void compute() {
        fun2.update(t, map);
        fun2.addInfo(t, list, map);
        fun2.compute(t, list);
    }
}
