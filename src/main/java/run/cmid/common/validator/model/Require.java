package run.cmid.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.validator.RegexModeInterface;
import run.cmid.common.validator.eumns.ValidationType;
import run.cmid.common.validator.annotations.FiledRequire;

import java.util.ArrayList;
import java.util.List;

/**
 * 生效的前置条件
 */
@Getter
@Setter
public class Require implements RegexModeInterface {
    private Require(FiledRequire filedRequires, String name) {
        this.fieldName = filedRequires.fieldName();
        this.message = filedRequires.message();
        this.mode = filedRequires.mode();
        this.value = filedRequires.value();
        this.regex = filedRequires.regex();
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
     * @param filedRequires
     * @param name          当前注解所在field的@NameField.value() 值
     */
    public static List<Require> builds(FiledRequire[] filedRequires, String name) {
        ArrayList<Require> list = new ArrayList<Require>();
        for (FiledRequire filedRequire : filedRequires) {
            list.add(Require.build(filedRequire, name));
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * @param filedRequire
     * @param name         当前注解所在field的@NameField.value() 值
     */
    public static Require build(FiledRequire filedRequire, String name) {
        return new Require(filedRequire, name);
    }
}
