package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.EnumName;

public enum FindModel implements EnumName {
    EQUALS("等于"),

    INCLUDE("包含"),
    ;

    FindModel(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    @Override
    public String getEnumName() {
        return null;
    }
}
