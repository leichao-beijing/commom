package run.cmdi.common.reader.register;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.register.RegisterAnnotationInterface;
import run.cmdi.common.register.anntications.ParameterStatic;
import run.cmdi.common.register.anntications.RegisterAnnotation;

import java.lang.reflect.Field;

@Getter
@Setter
public class info implements RegisterAnnotationInterface {

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    @ParameterStatic
    private String value;
    @ParameterStatic
    private Integer value1;

    private String fieldName;
    private Field field;

    @RegisterAnnotation
    public void info(Field field) {
        this.field = field;
    }
}
