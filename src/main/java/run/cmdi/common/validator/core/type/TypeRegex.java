package run.cmdi.common.validator.core.type;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum TypeRegex implements TypeName {
    REGEX("正则表达式"),

    ;
    @Getter
    String typeName;

    TypeRegex(String typeName) {
        this.typeName = typeName;
    }
}
