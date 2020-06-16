package run.cmid.common.reader.model.eumns;

import lombok.Getter;
import run.cmid.common.io.TypeName;

public enum CompareType implements TypeName {
    EQUALS("等于"),

    NO_EQUALS("不等于"),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    ;
    @Getter
    String typeName;

    CompareType(String typeName) {
        this.typeName = typeName;
    }
}
