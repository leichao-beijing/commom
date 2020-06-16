package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.TypeName;

/**
 * 
 * @author leichao
 */
@Getter
public enum TableState implements TypeName {

    NORMAL("正常"),

    DISCARD("废除");

    TableState(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    String typeName;
}
