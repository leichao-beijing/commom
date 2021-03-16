package run.cmdi.common.validator.core.type;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum TypeThanEnum implements TypeName {
    EQUALS("等于"),

    NO_EQUALS("不等于"),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    ;
    @Getter
    String typeName;

    TypeThanEnum(String typeName) {
        this.typeName = typeName;
    }
}
