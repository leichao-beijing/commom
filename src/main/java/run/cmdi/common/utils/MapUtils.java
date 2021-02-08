package run.cmdi.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapUtils {
    public static <T, A> void lineMap(Map<A, T> map, A a, Function<T, T> consumer) {
        map.put(a, consumer.apply(map.get(a)));
    }
}
