package run.cmdi.common.validator.core.type;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum TypeStringEnum implements TypeName {
    EQUALS("等于"),

    NO_EQUALS("不等于"),

    INCLUDE("包含"),

    NO_INCLUDE("不包含"),

    ;
    @Getter
    String typeName;

    TypeStringEnum(String typeName) {
        this.typeName = typeName;
    }
}
