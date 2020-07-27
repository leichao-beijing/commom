package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * @author leichao
 */
@Getter
public enum CooperateType implements TypeName {

    SINGLE("单项目合作"),

    MORE("多项目合作"),

    QUOTA("工时定额"),

    OWN("自有");

    CooperateType(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    private String typeName;

}
