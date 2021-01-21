package run.cmdi.common.convert;

import java.util.List;

public interface ConvertPage<T> {
    List<T> getValues(Integer index);

    List<List<T>> getAll();

    public int size();
}
