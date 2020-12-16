package run.cmdi.common.reader.pligins;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.plugin.PluginMainOne;
import run.cmdi.common.reader.annotations.FormatDate;
import run.cmdi.common.reader.annotations.IgnoreReader;
import run.cmdi.common.utils.ReflectLcUtils;
import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.validator.annotations.FieldNameList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ConvertPlugin<T> {
    public ConvertPlugin(String value, Field field) {
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

    public static <T> Map<String, PluginMainOne<ConvertPlugin<T>>> buildMap(Class<T> clazz) {
        return buildMap(clazz, null);
    }

    public static <T> Map<String, PluginMainOne<ConvertPlugin<T>>> buildMap(Class<T> clazz, List<PluginAnnotation> plugins) {
        Field[] fields = ReflectUtil.getFields(clazz);
        Map<String, PluginMainOne<ConvertPlugin<T>>> map = new LinkedHashMap<>();
        List<String> pluginsValue = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IgnoreReader.class))
                continue;
            FieldName fieldName = field.getAnnotation(FieldName.class);
            FieldNameList fieldNameList = field.getAnnotation(FieldNameList.class);
            FormatDate formatDate = field.getAnnotation(FormatDate.class);
            ConvertPlugin<T> convertPlugin;

            if (fieldNameList != null) {
                convertPlugin = new ConvertPluginList<T>(field.getName(), field, fieldNameList);
            } else if (fieldName != null)
                convertPlugin = new ConvertPlugin<T>(fieldName.value().equals("") ? field.getName() : fieldName.value(), field);
            else
                convertPlugin = new ConvertPlugin<T>(field.getName(), field);
            if (formatDate != null)
                convertPlugin.setFormat(formatDate.value());
            if (plugins != null)
                plugins.forEach((plugin) -> {
                    if (field.getAnnotation(plugin.getAnnotation()) != null)
                        pluginsValue.add(plugin.getName());
                });
            if (field.getType().isEnum()) {
                List<Field> list = ReflectLcUtils.getAnnotationInField(field.getType(), JsonValue.class);
                if (list.size() != 0)
                    convertPlugin.setEnumFieldName(list.get(0).getName());
            }
            PluginMainOne plugin = new PluginMainOne<>(convertPlugin, pluginsValue);
            map.put(field.getName(), plugin);
        }
        return map;
    }
}
