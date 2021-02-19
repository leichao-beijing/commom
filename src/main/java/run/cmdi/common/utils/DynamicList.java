package run.cmdi.common.utils;

import java.util.ArrayList;

public class DynamicList<T> extends ArrayList<T> {
    public DynamicList(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public DynamicList() {
        this.defaultValue = null;
    }

    private final T defaultValue;

    void dynamicAdd(int address, T t) {
        if (size() > address)//10 10
            add(address, t);
        else if (size() == address)
            add(t);
        else {
            int i = address - size() - 1;
            for (int i1 = 0; i1 < i; i1++)
                add(defaultValue);
            add(t);
        }
    }
}
