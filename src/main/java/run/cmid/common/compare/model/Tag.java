package run.cmid.common.compare.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class Tag {
    public Tag(Integer column) {
        this.column = column;
    }

    public Tag() {
    }

    private Integer column;
}
