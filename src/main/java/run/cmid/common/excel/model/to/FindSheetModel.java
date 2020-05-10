package run.cmid.common.excel.model.to;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.excel.model.FieldDetail;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class FindSheetModel<T> {
    public FindSheetModel(List<FieldDetail<T>> findHeadFiel) {
        this.findHeadFiel = findHeadFiel;
    }

    private Sheet findSheet;
    private final List<FieldDetail<T>> findHeadFiel;

}
