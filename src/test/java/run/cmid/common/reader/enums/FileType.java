package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumTypeName;

/**
 * @author leichao
 */
@Getter
public enum FileType implements EnumTypeName {
    PRODUCE_TABLE("产值表"),

    ORDER_TABLE("订单信息表"),
    ENGINEERING_TABLE("工程分类表"),

    PROVIDER_TABLE("建设单位表"),

    PROJECT_TABLE("项目表"),

    COOPERATE_PROVIDER_TABLE("合作单位简称对应表");

    private FileType(String typeName) {
        this.typeName = typeName;

    }

    @JsonValue
    private final String typeName;

}
