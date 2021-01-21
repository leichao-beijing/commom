package run.cmdi.common.validator.core;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import run.cmdi.common.utils.SpotPath;
import run.cmdi.common.validator.EngineClazz;
import run.cmdi.common.validator.EngineObject;
import run.cmdi.common.validator.FunctionClazzInterface;
import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.validator.exception.ValidatorFieldsException;
import run.cmdi.common.validator.model.MachModelInfo;
import run.cmdi.common.validator.model.ValidatorFieldException;
import run.cmdi.common.validator.plugins.RegisterDefaultTable;
import run.cmdi.common.validator.plugins.RegisterPastOrPresentTable;
import run.cmdi.common.validator.plugins.ValidatorPlugin;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorTools<T> implements FunctionClazzInterface<List<ValidatorPlugin>>, Validator<T> {

    public static <T> Validator<T> buildValidator(Class<T> t) {
        return new ValidatorTools<T>(t);
    }

    public static Validator buildValidator() {
        return new ValidatorTools();
    }

    private ValidatorTools(Class<T> t) {
        validationMap = new EngineClazz(t, this).getFieldMap();
    }

    private ValidatorTools() {
    }

    public ValidatorTools<T> addValidatorsMap(Map<String, List<ValidatorPlugin>> validationStringMap) {
        validationStringMap.forEach((key, value) -> {
            if (value == null)
                return;
            List<ValidatorPlugin> list = validationMap.get(key);
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
        List<ValidatorPlugin> list = validationMap.get(fieldName);
        if (list == null)
            list = new ArrayList<>();
        list.add(validatorPlugin);
        return this;
    }

    private Map<SpotPath, List<ValidatorPlugin>> validationMap = new HashMap<>();

    public List<ValidatorFieldException> validation(T t) {
        try {
            new EngineObject(t, validationMap, new ValidatorResultObject()).compute();
        } catch (ValidatorFieldsException e) {
            return e.getErr();
        }
        return new ArrayList<>();
    }

    public List<ValidatorFieldException> validationMap(Map<String, Object> data) {
        ValidatorResultObject v = new ValidatorResultObject();
        MachModelInfo info = new MachModelInfo(validationMap);
        data.forEach((key, value) -> {
            info.addValue(key, value);
        });
        return v.compute(null, info);
    }

    @Getter
    private List<String> overlapList;
    private Set<String> setFieldMap;

    @Override
    public List<ValidatorPlugin> resultField(SpotPath path, Field field) {
        validatorFieldName(field);
        List<ValidatorPlugin> list = new ArrayList<>();
        plugins.forEach((clazz) -> {
            ValidatorPlugin plugin = ReflectUtil.newInstanceIfPossible(clazz);
            plugin.instanceAnnotationInfo(field);
            if (plugin.isSupport()) {
                list.add(plugin);
                converterMap.put(field.getName(), plugin.isConverterException());
            }
        });
        return list;
    }

    @Getter
    private Map<String, Boolean> converterMap = new HashMap<>();

    public boolean isConverter(String fieldName) {
        Boolean bool = converterMap.get(fieldName);
        return (bool == null) ? true : bool;  //默认对异常进行响应
    }

    private void validatorFieldName(Field field) {
        if (overlapList == null)
            overlapList = new ArrayList<>();

        if (setFieldMap == null)
            setFieldMap = new HashSet<>();

        FieldName fieldName = field.getAnnotation(FieldName.class);
        String name;
        if (fieldName != null)
            name = fieldName.value();
        else
            name = field.getName();

        if (setFieldMap.contains(name))
            overlapList.add(name);
        else
            setFieldMap.add(name);
    }

    @Override
    public void validator(Map<SpotPath, List<ValidatorPlugin>> fieldMap) {

    }

    public Validator addPlugin(Class<? extends ValidatorPlugin> plugin) {
        plugins.add(plugin);
        return this;
    }


    private Set<Class<? extends ValidatorPlugin>> plugins = new HashSet<>() {{
        add(RegisterDefaultTable.class);
        add(RegisterPastOrPresentTable.class);
    }};
}
