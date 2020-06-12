package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * 
 * @author leichao
 */
@Getter
public enum TableState implements EnumName {

    NORMAL("正常"),

    DISCARD("废除");

    private TableState(String enumName) {
        this.enumName = enumName;
    }
    @JsonValue
    private String enumName;
}
