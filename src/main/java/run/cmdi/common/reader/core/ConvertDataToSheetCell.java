package run.cmdi.common.reader.core;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.poi.core.SheetUtils;
import run.cmdi.common.reader.annotations.CellPosition;
import run.cmdi.common.reader.annotations.CellPositionGroup;
import run.cmdi.common.reader.support.SupportConverterDate;
import run.cmdi.common.utils.ReflectLcUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.interfaces.RSAKey;
import java.util.*;

/**
 * @author leichao
 */

public class ConvertDataToSheetCell<T> {
    private Sheet sheet;
    private T t;
    /**
     * address与fieldName映射表
     */
    private FieldClazzGroupMode<String> fieldClazz = new FieldClazzGroupMode<>();

    private void validator(Field field, CellPosition cellPosition, FieldClazzGroupMode<String> error, FieldClazzGroupMode<String> override) {
        if (StringUtils.isBlack(cellPosition.value())) {
            error.put(cellPosition.group(), field.getName(), "isBlack");
            return;
        }
        try {
            new org.apache.poi.ss.util.CellAddress(cellPosition.value());
        } catch (NumberFormatException e) {
            error.put(cellPosition.group(), field.getName(), "NumberFormatException");
            return;
        }
        String fieldName = override.get(cellPosition.group(), cellPosition.value());
        if (fieldName != null) {
            error.put(cellPosition.group(), cellPosition.value(), field.getName() + "override " + fieldName);
            return;
        } else
            override.put(cellPosition.group(), cellPosition.value(), field.getName());
        fieldClazz.put(cellPosition.group(), cellPosition.value(), field.getName());
    }

    public ConvertDataToSheetCell(Class<T> t) {
        Field[] fields = ReflectUtil.getFields(t);
        FieldClazzGroupMode<String> error = new FieldClazzGroupMode<String>();
        FieldClazzGroupMode<String> override = new FieldClazzGroupMode<String>();
        for (Field field : fields) {
            CellPosition cellPosition = field.getAnnotation(CellPosition.class);
            if (cellPosition != null)
                validator(field, cellPosition, error, override);
            CellPositionGroup group = field.getAnnotation(CellPositionGroup.class);
            if (group != null)
                for (CellPosition cp : group.value()) {
                    validator(field, cp, error, override);
                }
        }
        if (!error.isEmpty())
            throw new NumberFormatException(error.toString());
        override.clear();
        error.clear();
    }

    public void writeSheet(Sheet sheet, T t) {
        writeSheet(sheet, t, Object.class);
    }

    public void writeSheet(Sheet sheet, T t, Class clazz) {
        Map<String, String> map = fieldClazz.get(clazz);
        if (map == null) return;
        map.forEach((key, value) -> {
            Object clazzValue = ReflectUtil.invoke(t, ReflectLcUtils.methodGetString(value));
            if (clazzValue == null) return;
            Field field = ReflectUtil.getField(t.getClass(), value);
            org.apache.poi.ss.util.CellAddress cellAddress = new org.apache.poi.ss.util.CellAddress(key);
            Cell cell = SheetUtils.getCreateCell(sheet, cellAddress.getRow(), cellAddress.getColumn());
            boolean state = true;
            for (PluginAnnotation supportConverter : supportConverters) {
                Annotation annotation = field.getAnnotation(supportConverter.getAnnotation());
                if (annotation == null)
                    continue;
                if (supportConverter.plugin(clazzValue, cell, annotation) == null)
                    state = false;
            }
            if (!state)
                return;
            converterDefault(clazzValue, cell);
        });
    }

    private void converterDefault(Object value, Cell cell) {
        if (value.getClass().isAssignableFrom(Date.class)) {
            cell.setCellValue((Date) value);
            return;
        } else if (NumberUtil.isNumber(value.toString())) {
            cell.setCellValue(NumberUtil.add(value.toString()).doubleValue());
            return;
        }
        cell.setCellValue(value.toString());
    }

    private List<PluginAnnotation> supportConverters = new ArrayList<>() {{
        add(new SupportConverterDate());
    }};
}

class FieldClazzGroupMode<T> {
    Map<Class, Map<String, T>> map = new HashMap<>();

    public Map<String, T> get(Class clazz) {
        return map.get(clazz);
    }

    public T get(Class clazz, String key) {
        Map<String, T> clazzMap = get(clazz);
        if (clazzMap == null)
            return null;
        return clazzMap.get(key);
    }

    public void put(Class clazz, String key, T value) {
        Map<String, T> clazzMap = map.get(clazz);
        if (clazzMap == null) {
            clazzMap = new HashMap<>();
            map.put(clazz, clazzMap);
        }
        clazzMap.put(key, value);
    }

    public void clear() {
        map.forEach((key, value) -> {
            value.clear();
        });
        map.clear();
    }

    public boolean isEmpty() {
        if (map.isEmpty())
            return true;
        Iterator<Map.Entry<Class, Map<String, T>>> it = map.entrySet().iterator();
        boolean state = true;
        while (it.hasNext()) {
            Map.Entry<Class, Map<String, T>> next = it.next();
            Map<String, T> value = next.getValue();
            if (!value.isEmpty())
                state = false;
        }
        return state;
    }
}