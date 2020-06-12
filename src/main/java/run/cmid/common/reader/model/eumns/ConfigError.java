package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.EnumName;

/**
 *
 * @author leichao
 * @date 2020-05-13 10:21:41
 */
public enum ConfigError implements EnumName {

    NO_SUPPORT_ENUM_CONFIG("不支持的配置"),
    
    LIST_ERROR_VALUE_IS_EMPTY("@ExcelConverterSimple.values不能没有数据"),
    
    METHOD_VALUES_IS_EMPTY("@Method.compare 内没有数据");

    private ConfigError(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getEnumName() {
        return typeName;
    }
}
