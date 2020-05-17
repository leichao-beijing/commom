package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumTypeName;

/**
 * 
 * @author leichao distribution
 */
@Getter
public enum ProduceType implements EnumTypeName {
    MACARO_STATION("基站-宏蜂窝"),

    LITTLE_STATION("基站-微蜂窝"),

    DISTRIBUTION_SYSTEM("分布系统");
    
    ProduceType(String typeName) {
        this.typeName = typeName;
    }
    @JsonValue
    private String typeName;
}
