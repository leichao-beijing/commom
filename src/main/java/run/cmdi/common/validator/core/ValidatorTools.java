package run.cmdi.common.validator.core;

import lombok.Getter;
import run.cmdi.common.validator.plugins.RegisterDefaultTable;
import run.cmdi.common.validator.plugins.RegisterPastOrPresentTable;
import run.cmdi.common.validator.plugins.ValidatorPlugin;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorTools<T> {

    public static <T> ValidatorTools<T> buildValidator(Class<T> t) {
        return new ValidatorTools<T>(t);
    }

    private final Class<T> t;

    private ValidatorTools(Class<T> t) {
        this.t = t;
    }

    private void config(Class<T> t) {
        plugins.forEach((pluginClass -> {
            Field[] fields = t.getDeclaredFields();
            for (Field field : fields) {
                try {
                    ValidatorPlugin plugin = pluginClass.getDeclaredConstructor().newInstance();
                    plugin.instanceAnnotationInfo(field);
                    List<ValidatorPlugin> list;
                    if ((list = mapPluginsConfig.get(field)) == null) {
                        mapPluginsConfig.put(field.getName(), list = new ArrayList<>());
                    }
                    list.add(plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public ValidatorTools<T> addValidatorsMap(Map<String, List<ValidatorPlugin>> validationStringMap) {
        validationStringMap.forEach((key, value) -> {
            if (value == null)
                return;
            List<ValidatorPlugin> list = mapPluginsConfig.get(key);
            if (list == null)
                list = new ArrayList<>();
            list.addAll(value);
        });
        return this;
    }

    /**
     * 添加扩展注解验证
     */
    public ValidatorTools<T> addValidator(String fieldName, ValidatorPlugin validatorPlugin) {
        List<ValidatorPlugin> list = mapPluginsConfig.get(fieldName);
        if (list == null)
            list = new ArrayList<>();
        list.add(validatorPlugin);
        return this;
    }

    private Map<String, List<ValidatorPlugin>> mapPluginsConfig = new HashMap<>();

    public Validator<T> buildValidator() throws Exception {
        config(this.t);
        return new Validator<T>(t, mapPluginsConfig);
    }

    @Getter
    private Map<String, Boolean> converterMap = new HashMap<>();

    public ValidatorTools<T> addPlugin(Class<? extends ValidatorPlugin> plugin) {
        plugins.add(plugin);
        return this;
    }

    private Set<Class<? extends ValidatorPlugin>> plugins = new HashSet<>() {{
        add(RegisterDefaultTable.class);
        add(RegisterPastOrPresentTable.class);
    }};
}
