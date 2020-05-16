package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.EnumTypeName;

/**
 * 
 * @author leichao
 */
public enum ExcelReadType implements EnumTypeName {

    INCLUDE("包含"), EQUALS("等于"), NO_INCLUDE("不包含"), NO_EQUALS("不等于");

    private ExcelReadType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }
}
