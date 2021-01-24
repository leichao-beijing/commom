package run.cmdi.common.reader.core;

import lombok.Getter;
import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ConvertOutPage;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.validator.core.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FieldInfos {
    public FieldInfos(Map<Integer, FieldInfo> map, ConvertOutPage page, FieldConfig config, int readHeadRownum) {
        this.map = map;
        this.page = page;
        this.readHeadRownum = readHeadRownum;
        this.config = config;
        config.getList().forEach(v -> {
            if (v.getType() != FieldDetailType.LIST)
                this.fieldNameMap.put(v.getFileName(), v);
        });
    }

    private int maxWrongCount;
    private boolean skipNoAnnotationField;
    private String bookTagName;
    private int startRow = -1;
    private boolean skipErrorResult = false;

    private final FieldConfig config;
    private final int readHeadRownum;
    private final ConvertOutPage<List> page;
    private Validator validator;

    //private ConverterException exception;
    private final Map<Integer, FieldInfo> map;
    private final Map<String, FieldInfo> fieldNameMap = new HashMap<>();

    public void setHeadInfo(ClazzBuildInfo convert) {
        this.skipNoAnnotationField = convert.isSkipNoAnnotationField();
        this.maxWrongCount = convert.getMaxWrongCount();
        this.bookTagName = convert.getBookTagName();
        validator = convert.getValidator();
    }

    public FieldInfo getFileInfo(String fieldName) {
        return fieldNameMap.get(fieldName);
    }
}
