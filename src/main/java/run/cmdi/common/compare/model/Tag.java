package run.cmdi.common.compare.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author leichao
 */
@Getter
@Setter
public class Tag {
    public Tag(Integer position) {
        this.position = position;
    }

    public Tag() {
    }

    private Integer position;
}
