package run.cmid.common.compare.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leichao
 * @date 2020-05-14 03:16:15
 */
@Getter
public class DataArray<VALUE, INFO> {
    public DataArray(VALUE value, INFO info) {
        this.value = value;
        this.info = info;
    }

    private List<VALUE> values;
    private List<INFO> infos;
    private VALUE value;
    private INFO info;

    public void add(VALUE value,INFO info) {
        if (values == null) {
            values = new ArrayList<>();
            values.add(this.value);
        }
        if (infos == null) {
            infos = new ArrayList<>();
            infos.add(this.info);
        }
        infos.add(info);
        values.add(value);
    }

    public Object getValue() {
        if (values == null) return value;
        return values;
    }
}
