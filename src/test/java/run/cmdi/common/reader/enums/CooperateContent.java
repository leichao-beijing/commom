package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * 合作内容
 *
 * @author leichao
 */
@Getter
public enum CooperateContent implements TypeName {
    NONE("/"),

    SCHEME("方案"),

    OTHER("其他"),

    NO_SEVEN_CORE("7个非核心工作环节"),

    CHECK("勘察"),

    INFO_COLLECT("信息采集"),

    COOPERATION("现场配合"),

    SUPPORT("支撑服务"),

    CHART_DRAW("图表绘制"),

    TEXT("文本整理"),

    TABLES("表格处理"),

    PUBLISH_FILE("文件出版"),

    DESIGNER("设计"),

    CHECK_DESIGNER("勘察+设计");

    CooperateContent(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    private String typeName;
}
