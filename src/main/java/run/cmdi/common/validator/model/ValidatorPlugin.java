package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.plugin.PluginAnnotation;

import javax.validation.constraints.PastOrPresent;
import java.lang.annotation.Annotation;
import java.util.Map;

@Getter
public class ValidatorPlugin {
    public ValidatorPlugin(PluginAnnotation<? extends  Annotation, Map<String, Object>> plugin, Annotation annotation) {
        this.plugin = plugin;
        this.annotation = annotation;
        name = plugin.getName();
    }

    private final String name;
    private final PluginAnnotation<? extends  Annotation, Map<String, Object>> plugin;
    private final Annotation annotation;
}
