package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumTypeName;

/**
 * 
 * @author leichao
 */
@Getter
public enum TableState implements EnumTypeName {

    NORMAL("正常"),

    DISCARD("废除");

    private TableState(String typeName) {
        this.typeName = typeName;
    }
    @JsonValue
    private String typeName;
}
