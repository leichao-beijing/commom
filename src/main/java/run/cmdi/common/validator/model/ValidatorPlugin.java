package run.cmdi.common.validator.model;

import lombok.Getter;
import run.cmdi.common.validator.plugins.ReaderPluginsInterface;

import java.lang.annotation.Annotation;

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
