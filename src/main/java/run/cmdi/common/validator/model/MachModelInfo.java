package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.utils.SpotPath;
import run.cmdi.common.validator.plugins.ValidatorPlugin;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MachModelInfo {
    public MachModelInfo(Map<SpotPath, List<ValidatorPlugin>> validatorMap) {
        this.validatorMap = validatorMap;
    }

    private final Map<SpotPath, List<ValidatorPlugin>> validatorMap;
    private final Map<String, ValueFieldName> valueContext = new HashMap<>();


    public void addValue(String fieldName, Object value) {
        List<ValidatorPlugin> list = validatorMap.get(fieldName);
        if (list == null || list.size() == 0)
            valueContext.put(fieldName, ValueFieldName.build(fieldName, fieldName, value));
        else
            valueContext.put(fieldName, ValueFieldName.build(fieldName, list.get(0).getName(), value));
    }
}
