package run.cmid.common.compare.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class CompareState<D> {
    public CompareState(D value) {
        this.value = value;
    }

    private boolean state = false;
    private final D value;
}
