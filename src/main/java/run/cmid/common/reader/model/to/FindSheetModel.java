package run.cmid.common.reader.model.to;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.FieldDetail;

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
