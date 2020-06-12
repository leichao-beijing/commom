package run.cmid.common.validator.eumns;

import run.cmid.common.io.EnumName;

/**
 * @author leichao
 * @date 2020-05-15 01:04:11
 */
public enum Value implements EnumName {

    NONE(""), NUMBER("数字类型");

    private final String typeName;

    Value(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getEnumName() {
        return typeName;
    }
}