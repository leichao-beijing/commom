package run.cmdi.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * @author leichao
 */
@Getter
public enum TagState implements TypeName {
    ADD("新增"),

    DELETE("删除"),

    DISCARD("废除"),

    NO_BAM("恢复"),

    REPLACE("修正"),//产值表

    ERROR("错误"),

    UPDATE("更新");

    TagState(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    private String typeName;
}
