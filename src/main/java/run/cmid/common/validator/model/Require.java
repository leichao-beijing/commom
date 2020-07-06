package run.cmid.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.eumns.ExcelRead;

/**
 * 生效的前置条件
 */
@Getter
@Setter
public class Require {
    String fieldName;
    String name;
    String[] value;
    ExcelRead model = ExcelRead.EQUALS;//regex
    /**
     * model== ExcelRead.NONE 时匹配正则 符合正则时生效。不存在时忽略 只验证转换对象的toString类型
     */
    String regex = "";
    String message = "";
}
