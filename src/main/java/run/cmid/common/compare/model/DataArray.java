package run.cmid.common.compare.model;

import lombok.Getter;

/**
 *
 * @author leichao
 * @date 2020-05-14 03:16:15
 */
@Getter
public class DataArray<DATA1, DATA2> {
    public DataArray(DATA1 data1, DATA2 data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    private DATA1 data1;
    private DATA2 data2;
}
