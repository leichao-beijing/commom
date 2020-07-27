package run.cmdi.common.reader.model.to;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.model.FieldDetail;

/**
 * @author leichao
 */
@Getter
@Setter
public class FindSheetModel<T> {
    public FindSheetModel(Map<String, FieldDetail> map) {
        this.map = map;
    }

    private Sheet findSheet;
    private final Map<String, FieldDetail> map;
}
