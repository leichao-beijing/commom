package run.cmdi.common.validator.core.type;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum TypeEnum implements TypeName {
    EQUALS("等于"),

    NO_EQUALS("不等于"),


    ;
    @Getter
    String typeName;

    TypeEnum(String typeName) {
        this.typeName = typeName;
    }
}
