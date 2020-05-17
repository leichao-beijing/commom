package run.cmid.common.reader.model.entity;

import java.util.List;

import lombok.Getter;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.to.AddressCell;

/**
 * 
 * @author leichao
 */
@Getter
public class FieldDetailErrorList<T> {
    public FieldDetailErrorList(LocationTag<T> tag, List<AddressCell<FieldDetail>> checkNullList) {
        this.tag = tag;
        this.checkNullList = checkNullList;
    }

    private LocationTag<T> tag;
    private List<AddressCell<FieldDetail>> checkNullList;
}
