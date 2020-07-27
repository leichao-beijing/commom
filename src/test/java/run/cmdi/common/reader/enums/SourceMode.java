package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum SourceMode implements TypeName {
    SOURCE("信源"),

    DISTRIBUTED("分布");

    SourceMode(String typeName) {
        this.typeName = typeName;
    }

    @Getter
    @JsonValue
    String typeName;
}