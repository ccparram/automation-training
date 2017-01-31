package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginLoader;
import pluggable.plugin.PluginRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * @author Juan Krzemien
 */
public class PluginRegistryImpl implements PluginRegistry, Logging {

    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();
    private final Set<PluginLoader> pluginLoaders = new HashSet<>();

    @Override
    public void register(Path pluginPath) {
        getLogger().info(format("Registering plugin %s...", pluginPath));
        String extension = getExtension(pluginPath.toFile().toString());
        pluginLoaders
                .stream()
                .filter(loader -> loader.handlesExtension(extension))
                .forEach(loader -> {
                    try {
                        loader.loadPlugin(pluginPath)
                                .ifPresent(plugin -> {
                                    plugins.put(pluginPath.toString(), plugin);
                                    plugin.load();
                                });
                    } catch (IOException e) {
                        getLogger().error(e.getLocalizedMessage(), e);
                    }
                });
    }

    @Override
    public void unregister(Path pluginPath) {
        getLogger().info(format("Unregistering plugin %s...", pluginPath));
        String asString = pluginPath.toString();
        Plugin plugin = plugins.get(asString);
        if (plugin != null) {
            plugin.unload();
            plugins.remove(asString);
        }
    }

    @Override
    public void unloadPlugins() {
        plugins.entrySet().parallelStream().forEach(entry -> unregister(Paths.get(entry.getKey())));
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
