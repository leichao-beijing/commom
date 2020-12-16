package run.cmdi.common.reader.pligins;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ValuePosition {
    public ValuePosition(String value, Integer index) {
        this.value = value;
        this.index = index;
    }

    /**
     * 对应list内序号
     */
    private final Integer index;
    /**
     * 对应的headName
     */
    private final String value;
    /**
     * cell内位置
     */
    @Setter
    private Integer position;
}
