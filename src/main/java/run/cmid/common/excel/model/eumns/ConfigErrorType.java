package run.cmid.common.excel.model.eumns;

import run.cmid.common.io.EnumTypeName;

/**
*
*@author    leichao
*@date 2020-05-13 10:21:41
*/
public enum ConfigErrorType implements EnumTypeName {

    NO_SUPPORT_ENUM_YYPE("不支持的配置");

    private ConfigErrorType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }
}
