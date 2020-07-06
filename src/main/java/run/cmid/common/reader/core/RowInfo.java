package run.cmid.common.reader.core;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class RowInfo {
    public RowInfo(int rownum, HashMap<String, Object> data) {
        this.rownum = rownum;
        this.data = data;
    }

    private HashMap<String, Object> data;
    private int rownum;
}
