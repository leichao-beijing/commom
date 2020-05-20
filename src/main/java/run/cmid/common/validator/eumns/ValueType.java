package run.cmid.common.validator.eumns;

import run.cmid.common.io.EnumTypeName;

/**
 * @author leichao
 * @date 2020-05-15 01:04:11
 */
public enum ValueType implements EnumTypeName {

    NONE(""), NUMBER("数字类型");

    private final String typeName;

    ValueType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}