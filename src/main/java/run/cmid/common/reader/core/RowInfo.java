package run.cmid.common.reader.core;

import lombok.Getter;
import run.cmid.common.compare.model.DataArray;
import run.cmid.common.reader.model.FieldDetail;

import java.util.Map;

@Getter
public class RowInfo {
    public  RowInfo(int rownum, Map<String,DataArray<Object, FieldDetail>> data){
        this.rownum=rownum;
        this.data=data;
    }
    Map<String, DataArray<Object, FieldDetail>> data;
    int rownum;
}
