package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * 工程类型
 * 
 * @author leichao
 */
@Getter
public enum EngineeringCost implements EnumName {
    NEW_SITE_INCLUDE_LOGIC_SITE("新建宏站（含逻辑站）"),

    DILATATION_REFORM("扩容或改造宏站"),

    INDOOR_SOURCE("室分信源"),

    TRADITION_SYSTEM("传统分布系统（天线数）"),

    NEW_TECHNIQUE("新技术室分系统（RRU/PRU数）");

    EngineeringCost(String enumName) {
        this.enumName = enumName;
    }

    @JsonValue
    private String enumName;

}
