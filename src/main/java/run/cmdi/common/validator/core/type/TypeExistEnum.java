package run.cmdi.common.validator.core.type;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum TypeExistEnum implements TypeName {
    EMPTY("空值"),

    EXIST("非空值"),

    ;
    @Getter
    String typeName;

    TypeExistEnum(String typeName) {
        this.typeName = typeName;
    }
}
