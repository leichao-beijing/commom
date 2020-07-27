package run.cmdi.common.validator.core;

import lombok.Getter;
import run.cmdi.common.utils.SpotPath;
import run.cmdi.common.validator.EngineClazz;
import run.cmdi.common.validator.EngineObject;
import run.cmdi.common.validator.FunctionClazzInterface;
import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.validator.annotations.FiledValidator;
import run.cmdi.common.validator.annotations.FiledValidators;
import run.cmdi.common.validator.exception.ValidatorErrorException;
import run.cmdi.common.validator.exception.ValidatorFieldsException;
import run.cmdi.common.validator.exception.ValidatorNullPointerException;
import run.cmdi.common.validator.exception.ValidatorOverlapException;
import run.cmdi.common.validator.model.*;
import run.cmdi.common.validator.plugins.ReaderPluginsInterface;
import run.cmdi.common.validator.plugins.ReaderPluginsPastOrPresent;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorTools<T> implements FunctionClazzInterface<ValidationMain> {
    public ValidatorTools(Class<T> t) throws ValidatorOverlapException {
        validationMap = new EngineClazz(t, this).getFieldMap();
        validatorException();
    }

    public ValidatorTools(Map<String, ValidationMain> validationStringMap) throws ValidatorErrorException {
        this.validationMap = EngineClazz.getSpotPathMap(validationStringMap);
        HashSet<String> set = new HashSet<>();
        validationMap.forEach((key, value) -> {
            if (value.getPluginAnnotationList() != null)
                value.getPluginAnnotationList().forEach((val) -> {
                    set.add(val);
                });
        });
        ArrayList<String> errorList = new ArrayList<>();
        set.forEach((value) -> {
            if (plugins.get(value) == null)
                errorList.add(value);
        });
        if (!errorList.isEmpty())
            throw new ValidatorNullPointerException(errorList);
    }

    private final Map<String, ReaderPluginsInterface> plugins = new HashMap<>() {{
        ReaderPluginsPastOrPresent readerPluginsPastOrPresent = new ReaderPluginsPastOrPresent();
        put(readerPluginsPastOrPresent.getName(), readerPluginsPastOrPresent);
    }};

    /**
     * 添加扩展注解验证
     */
    public ValidatorTools<T> addPlugins(ReaderPluginsInterface readerPluginsInterface) throws ValidatorErrorException {
        ReaderPluginsInterface plugin = plugins.get(readerPluginsInterface.getName());
        if (plugin != null) {
            throw new ValidatorOverlapException(new ArrayList<>() {{
                add(plugin.getName() + "=" + plugin.getClass().getName() + " and " + readerPluginsInterface.getName() + "=" + readerPluginsInterface.getClass().getName() + " Overlap.");
            }});
        }
        plugins.put(plugin.getName(), plugin);
        return this;
    }

    public void validatorException() throws ValidatorOverlapException {
        if (!overlapList.isEmpty())
            throw new ValidatorOverlapException(overlapList);
    }

    private Map<SpotPath, ValidationMain> validationMap;

    public List<ValidatorFieldException> validation(T t) {
        try {
            new EngineObject(t, validationMap, new ValidatorResultObject(pluginMap)).compute();
        } catch (ValidatorFieldsException e) {
            return e.getErr();
        }
        return new ArrayList<>();
    }

    public List<ValidatorFieldException> validationMap(Map<String, Object> context) {
        ValidatorResultObject v = new ValidatorResultObject(pluginMap);
        MachModelInfo info = new MachModelInfo(context, validationMap);
        return v.compute(null, info);
    }

    @Getter
    private List<String> overlapList;
    private Set<String> setFieldMap;

    @Override
    public ValidationMain resultField(SpotPath path, Field field) {
        validatorFieldName(field);
        return resultFieldFiledValidator(path, field);
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
    public void validator(Map<SpotPath, ValidationMain> fieldMap) {

    }

    private Map<String, List<ValidatorPlugin>> pluginMap = new HashMap<>();

    private ValidationMain resultFieldFiledValidator(SpotPath path, Field field) {
        List<MatchesValidation> list = new ArrayList<>();
        if (field.getAnnotation(FiledValidator.class) != null)
            list.add(new MatchesValidation(field.getAnnotation(FiledValidator.class), field));

        if (field.getAnnotation(FiledValidators.class) != null)
            for (FiledValidator filedValidator : field.getAnnotation(FiledValidators.class).value()) {
                list.add(new MatchesValidation(filedValidator, field));
            }
        List<ValidatorPlugin> pluginAnnotations = new ArrayList<>();
        ArrayList<String> pluginAnnotationList = new ArrayList<>();
        plugins.forEach((key, value) -> {
            if (field.isAnnotationPresent(value.getAnnotation())) {
                ValidatorPlugin validatorPlugin = new ValidatorPlugin(value, field.getAnnotation(value.getAnnotation()));
                pluginAnnotations.add(validatorPlugin);
                pluginAnnotationList.add(validatorPlugin.getName());
            }
        });
        if (!pluginAnnotations.isEmpty())
            pluginMap.put(field.getName(), pluginAnnotations);

        ValidationMain validationMain = new ValidationMain(list, pluginAnnotationList, field);
        return validationMain;
    }
}
