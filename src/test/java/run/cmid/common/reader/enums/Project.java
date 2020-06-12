package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * 
 * @author leichao
 */
@Getter
public enum Project implements EnumName {
    // 无线,有线,网络,电源,规划咨询,网络优化-小口径,网络优化-大修理,软件评估,招标代理,服务支撑,其他
    WIRELESS("无线"), CABLE("有线");

    Project(String enumName) {
        this.enumName = enumName;
    }

    @JsonValue
    private String enumName;

}
