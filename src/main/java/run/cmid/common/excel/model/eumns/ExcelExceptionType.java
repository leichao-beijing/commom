package run.cmid.common.excel.model.eumns;

import run.cmid.common.io.EnumTypeName;

/**
 * 
 * @author leichao
 */
public enum ExcelExceptionType implements EnumTypeName {
    SUCCESS("成功"),

    CUSTOM("自定义错误"),

    SHEET_NAME_NO_EXISTS("sheetName不存在"),
    
    CONVERT_ERROR("转换失败"),
    
    NUMBER_CONVERT_TYPE_ERROR("数字类型转换错误"),

    DATE_CONVERT_TYPE_ERROR("时间类型转换错误"),

    NO_CELL_TYPE_SUPPORT_TYPE("不支持的单元格类型"),

    NOT_FINDS_SHEET("没有找到与之匹配的Sheet"),

    FIND_FIELD_COUNT_WRONG("找到列的数量不符合要求"),

    NOT_FIND_CHECK_COLUMN("必选列不存在"),

    NOT_FIND_CHACK_VALUE("必选列内值不存在"),

    READER_HEAD_EMPTY("头sheet是空的"),

    INDEX_ERROR("行唯一性验证失败"),

    ENUM_ERROR("数据不再范围内"),
    
    INDEX_EMPTY("索引值不能为空"),

    //NO_FIND_METHOD("可执行方法为找到"),

    //LIST_STRING_ERROR1("ExcelConverterStringList 对应字符串没有匹配到"),

    STRING_OUT_BOUNDS("字符串长度超出限制");

    private ExcelExceptionType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        return typeName;
    }

}
