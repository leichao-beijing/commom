package run.cmid.common.compute.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.compute.enums.LinkState;
import run.cmid.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-05-03 11:56:59
 */
@ToString
public class ComputeInfo {
    public ComputeInfo(SpotPath path, String computeValue, List<SpotPath> computeList, Class<?> type) {
        this.path = path;
        this.computeValue = computeValue;
        if (computeValue != null)
            state = LinkState.COMPUTE;
        this.computeList = computeList;
        this.type = type;

    }

    public ComputeInfo(SpotPath path, SpotPath computeField, Class<?> type) {
        this.path = path;
        this.computeField = computeField;
        this.type = type;
    }

    @Getter
    private final SpotPath path;
    @Getter
    private final Class<?> type;
    @Getter
    private String computeValue;
    @Getter
    private SpotPath computeField;
    @Getter
    @Setter
    private LinkState state;
    @Getter
    @Setter
    private List<SpotPath> computeList;

}
