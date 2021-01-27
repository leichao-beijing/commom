package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.convert.RegisterAnnotationInterface;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.FindColumns;
import run.cmdi.common.reader.annotations.RegisterAnnotation;

import java.lang.reflect.Field;

@Getter
@Setter
public class FieldInfoAbstract implements RegisterAnnotationInterface {
    private String name;
    private String fieldName;
    private String format;

    @RegisterAnnotation
    public void findColumn(FindColumn findColumn) {
    }

    @RegisterAnnotation
    public void findColumns(FindColumns findColumn) {
    }

    @RegisterAnnotation
    public void findColumns(Field findColumn) {
    }
}
