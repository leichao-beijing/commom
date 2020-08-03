package run.cmdi.common.plugin;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 单一fieldName所承载的信息
 */
@Getter
@Setter
public class PluginMainOne<BODY> {

    public PluginMainOne(BODY body, List<String> plugins) {
        this.body = body;
        this.plugins = plugins;
    }

    private BODY body;
    private List<String> plugins;
}
