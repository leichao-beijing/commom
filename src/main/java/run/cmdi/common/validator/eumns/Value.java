package run.cmdi.common.validator.eumns;

import run.cmdi.common.io.TypeName;

/**
 * @author leichao
 * @date 2020-05-15 01:04:11
 */
public enum Value implements TypeName {

    NONE(""), NUMBER("数字类型");

    private final String typeName;

    Value(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}