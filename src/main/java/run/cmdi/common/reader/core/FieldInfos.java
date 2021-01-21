package run.cmdi.common.reader.core;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.convert.ConvertPage;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.eumns.FieldDetailType;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FieldInfos {
    public FieldInfos(Map<Integer, FieldInfo> map, ConvertPage page, FieldConfig config, int readHeadRownum) {
        this.map = map;
        this.page = page;
        this.readHeadRownum = readHeadRownum;
        this.config = config;
        config.getList().forEach(v -> {
            if (v.getType() != FieldDetailType.LIST)
                this.fieldNameMap.put(v.getFileName(), v);
        });
    }

    private final FieldConfig config;
    private final int readHeadRownum;
    private final ConvertPage page;
    private ConverterException exception;
    private final Map<Integer, FieldInfo> map;
    private final Map<String, FieldInfo> fieldNameMap = new HashMap<>();

    public FieldInfo getFileInfo(String fieldName) {
        return fieldNameMap.get(fieldName);
    }
}
