package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * 
 * @author leichao
 */
@Getter
public enum Cooperate implements EnumName {

    SINGLE("单项目合作"),

    MORE("多项目合作"),

    QUOTA("工时定额"),

    OWN("自有");
    Cooperate(String enumName) {
        this.enumName = enumName;
    }
    @JsonValue
    private String enumName;

}
