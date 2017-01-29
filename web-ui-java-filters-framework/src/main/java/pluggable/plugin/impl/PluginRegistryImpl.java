package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginRegistry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class PluginRegistryImpl implements PluginRegistry, Logging {

    private final Map<Path, Plugin> plugins = new ConcurrentHashMap<>();
    private final PluginLoaderImpl pluginLoader = new PluginLoaderImpl();

    @Override
    public void register(Path pluginJarPath) {
        getLogger().info(format("Registering plugin %s...", pluginJarPath));
        try {
            pluginLoader.loadPlugin(pluginJarPath).ifPresent(plugin -> {
                plugins.put(pluginJarPath, plugin);
                plugin.load();
            });
        } catch (Exception e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void unregister(Path pluginJarPath) {
        getLogger().info(format("Unregistering plugin %s...", pluginJarPath));
        Plugin plugin = plugins.get(pluginJarPath);
        if (plugin != null) {
            plugin.unload();
            plugins.remove(pluginJarPath);
        }
    }

    @Override
    public void unloadPlugins() {
        plugins.entrySet().parallelStream().forEach(entry -> unregister(entry.getKey()));
        plugins.clear();
    }

    @Override
    public Collection<Plugin> getPlugins() {
        getLogger().info("Retrieving newest list of plugins...");
        return new ArrayList<>(plugins.values());
    }


}
