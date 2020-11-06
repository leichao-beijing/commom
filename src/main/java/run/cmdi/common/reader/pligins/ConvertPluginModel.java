package run.cmdi.common.reader.pligins;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.plugin.PluginMainOne;
import run.cmdi.common.reader.annotations.IgnoreReader;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.annotations.FieldName;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
public class ConvertPluginModel<T> {
    public ConvertPluginModel(String value, Field field) {
        this.fieldName = field.getName();
        this.value = value;
        this.field = field;
    }

    private Field field;
    private String fieldName;
    private String value;
    private Integer position;
    private String format;
    private String enumFieldName = "";

    public static <T> Map<String, PluginMainOne<ConvertPluginModel<T>>> buildMap(Class<T> clazz) {
        return buildMap(clazz, null);
    }

    public static <T> Map<String, PluginMainOne<ConvertPluginModel<T>>> buildMap(Class<T> clazz, List<PluginAnnotation> plugins) {
        Field[] fields = ReflectUtil.getFields(clazz);
        Map<String, PluginMainOne<ConvertPluginModel<T>>> map = new LinkedHashMap<>();
        List<String> pluginsValue = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IgnoreReader.class))
                continue;
            FieldName fieldName = field.getAnnotation(FieldName.class);
            ConvertPluginModel<T> convertPluginModel;

            if (fieldName != null)
                convertPluginModel = new ConvertPluginModel<T>(fieldName.value().equals("") ? field.getName() : fieldName.value(), field);
            else
                convertPluginModel = new ConvertPluginModel<T>(field.getName(), field);
            if (plugins != null)
                plugins.forEach((plugin) -> {
                    if (field.getAnnotation(plugin.getAnnotation()) != null)
                        pluginsValue.add(plugin.getName());
                });
            if (field.getType().isEnum()) {
                List<Field> list = ReflectLcUtils.getAnnotationInField(field.getType(), JsonValue.class);
                if (list.size() != 0)
                    convertPluginModel.setEnumFieldName(list.get(0).getName());
            }
            PluginMainOne plugin = new PluginMainOne<>(convertPluginModel, pluginsValue);
            map.put(field.getName(), plugin);
        }
        return map;
    }
}
