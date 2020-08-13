package run.cmdi.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.RegexModeInterface;
import run.cmdi.common.validator.annotations.FieldRequire;

import java.util.ArrayList;
import java.util.List;

/**
 * 生效的前置条件
 */
@Getter
@Setter
public class Require implements RegexModeInterface {
    private Require(FieldRequire fieldRequires, String name) {
        this.fieldName = fieldRequires.fieldName();
        this.message = fieldRequires.message();
        this.mode = fieldRequires.mode();
        this.value = fieldRequires.value();
        this.regex = fieldRequires.regex();
        this.name = name;
    }

    String fieldName;
    String name;
    String[] value;
    ValidationType mode = ValidationType.EQUALS;//regex
    /**
     * model== ExcelRead.NONE 时匹配正则 符合正则时生效。不存在时忽略 只验证转换对象的toString类型
     */
    String regex = "";
    String message = "";

    /**
     * @param fieldRequires
     * @param name          当前注解所在field的@NameField.value() 值
     */
    public static List<Require> builds(FieldRequire[] fieldRequires, String name) {
        ArrayList<Require> list = new ArrayList<Require>();
        for (FieldRequire fieldRequire : fieldRequires) {
            list.add(Require.build(fieldRequire, name));
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * @param fieldRequire
     * @param name         当前注解所在field的@NameField.value() 值
     */
    public static Require build(FieldRequire fieldRequire, String name) {
        return new Require(fieldRequire, name);
    }
}
