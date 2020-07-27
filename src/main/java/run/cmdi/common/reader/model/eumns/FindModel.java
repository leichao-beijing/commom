package run.cmdi.common.reader.model.eumns;

import run.cmdi.common.io.TypeName;

public enum FindModel implements TypeName {
    EQUALS("等于"),

    INCLUDE("包含"),
    ;

    FindModel(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    @Override
    public String getTypeName() {
        return null;
    }
}
