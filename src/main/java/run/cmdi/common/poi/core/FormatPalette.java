package run.cmdi.common.poi.core;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class FormatPalette {
    private final Workbook workbook;
    private final Map<String, Short> map = new HashMap<>();

    public FormatPalette(Workbook workbook) {
        this.workbook = workbook;
    }

    public short getFormatIndex(String format) {
        Short value = map.get(format);
        if (value != null)
            return value;
        value = formatIndex(format);
        map.put(format, value);
        return value;
    }

    private short formatIndex(String format) {
        return 1;
    }
}
