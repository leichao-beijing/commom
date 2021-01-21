package run.cmdi.common.reader.core;

import lombok.Getter;

import java.util.Map;

@Getter
public class RowInfo {
    public RowInfo(int rownum, Map<String, Object> data) {
        this.rownum = rownum;
        this.data = data;
    }

    private Map<String, Object> data;
    private int rownum;
}
