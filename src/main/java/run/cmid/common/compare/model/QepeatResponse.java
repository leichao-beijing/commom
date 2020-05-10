package run.cmid.common.compare.model;

import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author leichao
 */
@Getter
@ToString
public class QepeatResponse {
    public QepeatResponse(int qepeatIndex, int index) {
        this.index = index;
        this.qepeatIndex = qepeatIndex;
    }

    private final int qepeatIndex;
    private final int index;
}
