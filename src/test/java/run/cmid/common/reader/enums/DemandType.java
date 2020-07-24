package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.TypeName;

/**
 * "新建", "扩容", "大修", "升级", "替换"
 */
public enum DemandType implements TypeName {
    CREATE("新建"),

    UPDATE("升级"),

    EXTEND("扩容"),

    FIX("大修"),

    REPLACE("替换");

    DemandType(String typeName) {
        this.typeName = typeName;
    }

    @Getter
    @JsonValue
    String typeName;
}
