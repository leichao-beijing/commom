package run.cmdi.common.validator.core;

import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.utils.SpotPath;
import run.cmdi.common.validator.plugins.ValidatorPlugin;
import run.cmdi.common.validator.plugins.ValueFieldName;
import run.cmdi.common.validator.exception.ValidatorFieldsException;
import run.cmdi.common.validator.model.*;
import run.cmdi.common.validator.ResultObjectInterface;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorResultObject<T> implements ResultObjectInterface<T, List<ValidatorPlugin>, MachModelInfo> {
    //private final Set<Class<? extends ValidatorPlugin>> plugins;

    public ValidatorResultObject() {

    }

    @Override
    public void update(T t, Map<SpotPath, List<ValidatorPlugin>> map) {

    }

    @Override
    public void addInfo(T t, List<MachModelInfo> list, Map<SpotPath, List<ValidatorPlugin>> validatorMap) {
        MachModelInfo info = new MachModelInfo(validatorMap);
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object computeValue = ReflectLcUtils.getObjectValue(t, field.getName());
            info.addValue(field.getName(), computeValue);
        }
        list.add(info);
    }

    @Override
    public void compute(T t, List<MachModelInfo> list) {
        List<ValidatorFieldException> err = new ArrayList<ValidatorFieldException>();
        for (MachModelInfo machModelInfo : list) {
            err.addAll(compute(t, machModelInfo));
        }
        if (err.size() != 0)
            throw new ValidatorFieldsException(err);
    }

    public List<ValidatorFieldException> compute(T t, MachModelInfo machModelInfo) {
        Map<SpotPath, List<ValidatorPlugin>> map = machModelInfo.getValidatorMap();
        Map<String, ValueFieldName> context = machModelInfo.getValueContext();
        Iterator<Map.Entry<SpotPath, List<ValidatorPlugin>>> it = map.entrySet().iterator();
        List<ValidatorFieldException> err = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<SpotPath, List<ValidatorPlugin>> next = it.next();
            List<ValidatorPlugin> validatorPlugins = next.getValue();
            if (validatorPlugins == null || validatorPlugins.size() == 0)
                continue;
            String fieldName = validatorPlugins.get(0).getFieldName();
            ValueFieldName valueFieldName = context.get(fieldName);
            if (valueFieldName == null)
                valueFieldName = ValueFieldName.build(fieldName);
            for (ValidatorPlugin validatorPlugin : validatorPlugins) {
                err.addAll(validatorPlugin.validator(valueFieldName, context));
            }
        }
        return err;
    }
}
