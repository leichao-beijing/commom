package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.TypeName;

/**
 * 
 * @author leichao
 */
public enum FieldException implements TypeName {
    NOT_SUPPORT_FIELD_TYPE("不支持这个Field对象"),

    FIELD_TYPE_OVERRIDE("注解重复"),

    INITIALIZATION_ERROR("初始化错误");

    private FieldException(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }

}
