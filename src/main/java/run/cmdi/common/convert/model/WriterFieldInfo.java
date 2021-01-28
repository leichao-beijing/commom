package run.cmdi.common.convert.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.convert.RegisterAnnotationInterface;
import run.cmdi.common.validator.annotations.FieldName;

import java.lang.reflect.Field;

@Getter
@Setter
public class WriterFieldInfo implements RegisterAnnotationInterface {
    private String fieldName;
    private String name;

    public void info(FieldName name) {
        this.name = name.value();
    }

    public void info(Field field) {
        this.name = field.getName();
    }


}
