package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum YesNoState implements TypeName {
    YES("是"), NO("否");

    YesNoState(String typeName) {
        this.typeName = typeName;
    }

    @Getter
    @JsonValue
    private String typeName;

}
