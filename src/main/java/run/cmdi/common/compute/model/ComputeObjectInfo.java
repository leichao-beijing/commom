package run.cmdi.common.compute.model;

import java.util.Map;

import lombok.Getter;

/**
 * @author leichao
 * @date 2020-05-05 02:07:02
 */
@Getter
public class ComputeObjectInfo {
    private final Object parentObject;
    private final ComputeInfo info;
    private final Map<String, Object> mapContext;

    public ComputeObjectInfo(Object parentObject, ComputeInfo info, Map<String, Object> mapContext) {
        this.mapContext = mapContext;
        this.parentObject = parentObject;
        this.info = info;
    }
}
