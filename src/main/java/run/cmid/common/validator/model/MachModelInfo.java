package run.cmid.common.validator.model;

import lombok.Getter;
import run.cmid.common.utils.SpotPath;

import java.util.List;
import java.util.Map;

@Getter
public class MachModelInfo {
    public MachModelInfo(Map<String, Object> mapContext, Map<SpotPath, List<MatchesValidation>> map) {
        this.mapContext = mapContext;
        this.map = map;
    }

    private final Map<SpotPath, List<MatchesValidation>> map;
    private final Map<String, Object> mapContext;
}
