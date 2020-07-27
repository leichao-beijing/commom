package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

public enum NetworkMode implements TypeName {
    G2("2G微蜂窝"),

    G3("3G微蜂窝"),

    G4("4G微蜂窝"),

    G5("5G微蜂窝"),

    MICRO_CELL("微蜂窝"),

    WLAN("WLAN");

    NetworkMode(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    @Getter
    private String typeName;
}
