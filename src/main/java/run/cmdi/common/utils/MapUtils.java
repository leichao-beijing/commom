package run.cmdi.common.utils;

import java.util.Map;
import java.util.function.Function;

public class MapUtils {
    public static <T, A> void lineMap(Map<A, T> map, A a, Function<T, T> consumer) {
        T value = consumer.apply(map.get(a));
        if (value != null)
            map.put(a, consumer.apply(map.get(a)));
    }
}
