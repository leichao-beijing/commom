package run.cmdi.common.plugin;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单一fieldName所承载的信息
 */
@Getter
@Setter
public class PluginMain<BODY> implements PluginMainInterface<BODY> {
    public PluginMain(List<BODY> body, List<String> plugins) {
        this.plugins = plugins;
        this.body = body;
    }

    public PluginMain(BODY body, List<String> plugins) {
        this.body = new ArrayList<>();
        this.body.add(body);
        this.plugins = plugins;
    }

    private List<BODY> body;
    private List<String> plugins;
}
