package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ConvertOutPage;
import run.cmdi.common.reader.core.FindFieldConfig;
import run.cmdi.common.validator.core.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FindFieldInfos {
    public FindFieldInfos(Map<Integer, FindFieldInfo> map, ConvertOutPage page, FindFieldConfig config, int readHeadRownum) {
        this.map = map;
        this.page = page;
        this.readHeadRownum = readHeadRownum;
        this.config = config;
    }

    @Setter
    private boolean state = false;

    private int maxWrongCount;
    private boolean skipNoAnnotationField;
    private String bookTagName;
    private int startRow = -1;
    private boolean skipErrorResult = false;

    private final FindFieldConfig config;
    private final int readHeadRownum;
    private final ConvertOutPage<List> page;
    private Validator validator;

    private final Map<Integer, FindFieldInfo> map;

    public void setHeadInfo(ClazzBuildInfo convert) {
        this.skipNoAnnotationField = convert.isSkipNoAnnotationField();
        this.maxWrongCount = convert.getMaxWrongCount();
        this.bookTagName = convert.getBookTagName();
        validator = convert.getValidator();
    }

    public FindFieldInfo getFileInfo(String fieldName) {
        return config.getMap().get(fieldName);
    }
}
