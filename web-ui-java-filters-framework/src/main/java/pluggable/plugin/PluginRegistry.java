package pluggable.plugin;

import java.nio.file.Path;
import java.util.Collection;

/**
 * @author Juan Krzemien
 */
public interface PluginRegistry {

    void register(Path pluginJarPath);

    void unregister(Path pluginJarPath);

    void unloadPlugins();

    Collection<Plugin> getPlugins();

    void addPluginLoader(PluginLoader pluginLoader);

}
