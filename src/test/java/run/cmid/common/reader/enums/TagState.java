package run.cmid.common.reader.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import run.cmid.common.io.EnumName;

/**
 * 
 * @author leichao
 */
@Getter
public enum TagState implements EnumName {
    ADD("新增"),

    DELETE("删除"),

    DISCARD("废除"),

    NO_BAM("恢复"),

    REPLACE("修正"),//产值表

    ERROR("错误"),

    UPDATE("更新");

    TagState(String enumName) {
        this.enumName = enumName;
    }
    @JsonValue
    private String enumName;

//    public static List<String> getTypeNameList() {
//        ArrayList<String> list = new ArrayList<String>();
//        for (TagStart tagStart : values()) {
//            list.add(tagStart.getTypeName());
//        }
//        return list;
//    }
//
//    public static TagStart isTypeName(String typeName) {
//        for (TagStart tagStart : values()) {
//            if (tagStart.getTypeName().equals(typeName))
//                return tagStart;
//        }
//        return null;
//    }
}
