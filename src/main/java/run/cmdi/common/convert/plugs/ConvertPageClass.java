package run.cmdi.common.convert.plugs;

import run.cmdi.common.convert.ConvertPage;

import java.util.List;

public class ConvertPageClass<T> implements ConvertPage<T> {

    public ConvertPageClass(List<ConvertPage> list, Class<T> clazz) {
    }

    @Override
    public List<T> getValues(Integer index) {
        return null;
    }

    @Override
    public List<List<T>> getAll() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
