package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * @author leichao
 */
@Getter
public enum File implements TypeName {
    PRODUCE_TABLE("产值表"),

    ORDER_TABLE("订单信息表"),
    ENGINEERING_TABLE("工程分类表"),

    PROVIDER_TABLE("建设单位表"),

    PROJECT_TABLE("项目表"),

    COOPERATE_PROVIDER_TABLE("合作单位简称对应表");

    File(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    String typeName;

}
