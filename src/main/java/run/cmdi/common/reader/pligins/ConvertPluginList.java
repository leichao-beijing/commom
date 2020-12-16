package run.cmdi.common.reader.pligins;

import lombok.Getter;
import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.validator.annotations.FieldNameList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConvertPluginList<T> extends ConvertPlugin<T> {
    public ConvertPluginList(String value, Field field, FieldNameList fieldNameList) {
        super(value, field);
        int i = 0;
        for (FieldName fieldName : fieldNameList.value()) {
            list.add(new ValuePosition(fieldName.value(), i));
            i++;
        }
    }

    @Getter
    private List<ValuePosition> list = new ArrayList<>();
}
