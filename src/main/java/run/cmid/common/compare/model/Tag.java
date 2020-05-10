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
    public Tag(Integer rowmun) {
        this.rowmun = rowmun;
    }

    public Tag() {
    }

    private Integer rowmun;
}
