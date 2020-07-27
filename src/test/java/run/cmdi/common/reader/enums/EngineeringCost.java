package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * 工程类型
 *
 * @author leichao
 */
@Getter
public enum EngineeringCost implements TypeName {
    NEW_SITE_INCLUDE_LOGIC_SITE("新建宏站（含逻辑站）"),

    DILATATION_REFORM("扩容或改造宏站"),

    INDOOR_SOURCE("室分信源"),

    TRADITION_SYSTEM("传统分布系统（天线数）"),

    NEW_TECHNIQUE("新技术室分系统（RRU/PRU数）");

    EngineeringCost(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    private String typeName;

}
