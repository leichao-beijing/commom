package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.EnumTypeName;

/**
 *
 * @author leichao
 * @date 2020-05-13 10:21:41
 */
public enum ConfigErrorType implements EnumTypeName {

    NO_SUPPORT_ENUM_YYPE("不支持的配置"),
    
    LIST_ERROR_VALUE_IS_EMPTY("@ExcelConverterSimple.values不能没有数据"),
    
    METHOD_VALUES_IS_EMPTY("@Method.values 内没有数据");

    private ConfigErrorType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }
}
