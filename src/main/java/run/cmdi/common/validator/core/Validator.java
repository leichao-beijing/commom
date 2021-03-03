package run.cmdi.common.validator.core;

import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.register.RegisterAnnotationUtils;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.model.VerificationResult;
import run.cmdi.common.validator.plugins.ValidatorPlugin;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.lang.reflect.Field;
import java.util.*;

public class Validator<T> {
    public Validator(Class<T> t, Map<String, List<ValidatorPlugin>> mapPluginsConfig) throws Exception {
        this.valueFieldMap = RegisterAnnotationUtils.build(t.getClass(), ValueFieldName.class, null, false);
        this.mapPluginsConfig = mapPluginsConfig;
    }

    private final Map<String, List<ValidatorPlugin>> mapPluginsConfig;
    private final Map<String, ValueFieldName> valueFieldMap;

    public VerificationResult validation(T t) {
        Map<String, Object> values = buildMap(t);
        Map<String, ValueFieldName> map = new HashMap<>();
        values.forEach((key, value) -> {
            ValueFieldName val = valueFieldMap.get(key);
            if (val == null)
                val = ValueFieldName.build(key);
            val.build(value);
            map.put(key, val);
        });

        return validation(map);
    }

    public VerificationResult validation(Map<String, ValueFieldName>  data) {
        Iterator<Map.Entry<String, List<ValidatorPlugin>>> it = mapPluginsConfig.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ValidatorPlugin>> next = it.next();
            String filedName = next.getKey();
            List<ValidatorPlugin> list = next.getValue();
            if (list == null || list.size() == 0)
                continue;
            for (ValidatorPlugin validatorPlugin : list) {
                validatorPlugin.validator(data);
            }

        }
        return null;
    }


    private static Map<String, Object> buildMap(Object t) {
        Field[] fields = t.getClass().getDeclaredFields();
        List<Field> list = Arrays.asList(fields);
        Map<String, Object> map = new HashMap<>();
        for (Field field : list) {
            String name = ReflectLcUtils.methodGetString(field.getName());
            Object value = null;
            try {
                value = ReflectUtil.invoke(t, name);
            } catch (Exception e) {
            }
            map.put(field.getName(), value);
        }
        return map;
    }
}

