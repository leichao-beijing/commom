package run.cmdi.common.validator.plugins;

import run.cmdi.common.validator.annotations.support.FieldName;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface ValidatorPlugin extends FieldName {
    void instanceAnnotationInfo(Field field);

    boolean isSupport();

    List<ValidatorFieldException> validator(ValueFieldName value,Map<String, ValueFieldName> context);
}
