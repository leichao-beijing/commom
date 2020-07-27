package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.utils.SpotPath;

import java.util.Map;

@Getter
public class MachModelInfo {
    public MachModelInfo(Map<String, Object> mapContext, Map<SpotPath, ValidationMain> map) {
        this.mapContext = mapContext;
        this.map = map;
    }

    private final Map<SpotPath, ValidationMain> map;
    private final Map<String, Object> mapContext;
}
