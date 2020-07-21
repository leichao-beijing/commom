package run.cmid.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.validator.plugins.ReaderPluginsInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Getter
public class ValidatorPlugin {
    public ValidatorPlugin( ReaderPluginsInterface plugin, Annotation annotation) {
        this.plugin = plugin;
        this.annotation = annotation;
        name = plugin.getName();
    }

    private final String name;
    private final ReaderPluginsInterface plugin;
    private final Annotation annotation;
}
