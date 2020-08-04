package run.cmdi.common.plugin;

import java.util.List;

public interface PluginMainInterface<BODY> {
    List<BODY> getBody();

    List<String> getPlugins();
}
