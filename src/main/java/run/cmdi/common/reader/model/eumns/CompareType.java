package run.cmdi.common.reader.model.eumns;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum CompareType implements TypeName {
    EQUALS("等于"),

    NO_EQUALS("不等于"),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    NO_EMPTY("非空值"),

    EMPTY("空值"),

    INCLUDE("包含"),

    NO_INCLUDE("不包含"),
    ;
    @Getter
    String typeName;

    CompareType(String typeName) {
        this.typeName = typeName;
    }
}
