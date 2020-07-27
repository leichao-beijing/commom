package run.cmdi.common.validator.eumns;

import run.cmdi.common.io.TypeName;

public enum ValidatorErrorType implements TypeName {
    COMPARE_IS_EMPTY("当mode!=ExcelRead.EXISTS EMPTY时，比较的内容是不允许为空。"),

    CUSTOM("自定义错误"),

    //ENUM_ERROR("数据不再范围内"),

    VALIDATOR_ERROR("逻辑验证失败"),

    SIZE_ERROR("数量不在范围内"),

    LENGTH_ERROR("长度不在范围内"),

    NO_NUMBER("不是数字"),

    ON_EMPTY("不能是空值"),

    EMPTY("不能存在值"),

    REGEX_EMPTY("正则验证内容不能为空"),

    NUMBER_NULL("数字不能是null") ;

    ValidatorErrorType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }
}