package run.cmdi.common.reader.core;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.reader.model.eumns.FindModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FieldInfo {
    public FieldInfo(FindColumn findColumn, String fileName, Integer listIndex) {
        this(findColumn, fileName);
        this.listIndex = listIndex;
        this.type = FieldDetailType.LIST;
    }

    public FieldInfo(FindColumn findColumn, String fileName) {
        for (String s : findColumn.value()) {
            values.add(s);
        }
        this.fileName = fileName;
        this.findModel = findColumn.model();
        this.checkColumn = findColumn.checkColumn();
    }

    private String name;
    private final String fileName;
    private String enumFieldName;
    private FieldDetailType type = FieldDetailType.SINGLE;
    private FindModel findModel;
    private List<String> values = new ArrayList<>();
    private boolean checkColumn;
    private Integer index;
    private Integer listIndex;
    private String formatDate;
    private boolean state = false;
    private boolean converterException = false;

    public boolean match(String value) {
        switch (findModel) {
            case INCLUDE:
                for (String s : values) {
                    if (s.indexOf(value) != -1)
                        return true;
                }
                return false;
            case EQUALS:
                return values.contains(value);
        }
        return false;
    }

    public void exception(Exception e) throws ConverterExcelException {
        if (converterException)
            throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, e.getMessage());
    }

    public void exception(Object value) throws ConverterExcelException {
        if (value == null && converterException)
            throw new ConverterExcelException(ConverterErrorType.CONVERT_ERROR, name + " 数据:为null" +
                    ((formatDate != null) ? "支持要求" + formatDate : ""));
    }
}
