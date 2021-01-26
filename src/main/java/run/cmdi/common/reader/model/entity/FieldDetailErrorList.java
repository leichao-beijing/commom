package run.cmdi.common.reader.model.entity;

import java.util.List;

import lombok.Getter;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.reader.model.FieldDetailOld;
import run.cmdi.common.reader.model.to.AddressCell;

/**
 * @author leichao
 */
@Getter
public class FieldDetailErrorList<T> {
    public FieldDetailErrorList(LocationTag<T> tag, List<AddressCell<FieldDetailOld>> checkNullList) {
        this.tag = tag;
        this.checkNullList = checkNullList;
    }

    private LocationTag<T> tag;
    private List<AddressCell<FieldDetailOld>> checkNullList;
}
