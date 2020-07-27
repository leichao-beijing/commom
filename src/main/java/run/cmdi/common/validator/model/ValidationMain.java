package run.cmdi.common.validator.model;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.List;

@Getter
public class ValidationMain {
    public ValidationMain(List<MatchesValidation> validations, List<String> pluginAnnotationList, Field field) {
        this.field = field;
        this.fieldName = field.getName();
        this.validations = validations;
        this.pluginAnnotationList = pluginAnnotationList;
    }

    private final String fieldName;
    private final Field field;
    private final List<MatchesValidation> validations;
    private final List<String> pluginAnnotationList;
}
