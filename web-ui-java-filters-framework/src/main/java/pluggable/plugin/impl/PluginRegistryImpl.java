package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import org.apache.commons.io.FilenameUtils;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginLoader;
import pluggable.plugin.PluginRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class PluginRegistryImpl implements PluginRegistry, Logging {

    private final Map<Path, Plugin> plugins = new ConcurrentHashMap<>();
    private final Set<PluginLoader> pluginLoaders = new HashSet<>();

    @Override
    public void register(Path pluginPath) {
        String extension = getExtension(pluginPath);
        getLogger().info(format("Registering plugin %s...", pluginPath));
        pluginLoaders
                .stream()
                .filter(loader -> loader.handlesExtension(extension))
                .forEach(loader -> {
                    try {
                        loader.loadPlugin(pluginPath)
                                .ifPresent(plugin -> {
                                    plugins.put(pluginPath, plugin);
                                    plugin.load();
                                });
                    } catch (IOException e) {
                        getLogger().error(e.getLocalizedMessage(), e);
                    }
                });
    }

    private String getExtension(Path pluginPath) {
        return FilenameUtils.getExtension(pluginPath.toFile().toString());
    }

    @Override
    public void unregister(Path pluginPath) {
        getLogger().info(format("Unregistering plugin %s...", pluginPath));
        Plugin plugin = plugins.get(pluginPath);
        if (plugin != null) {
            plugin.unload();
            plugins.remove(pluginPath);
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

    @Override
    public void addPluginLoader(PluginLoader pluginLoader) {
        pluginLoaders.add(pluginLoader);
    }

}
