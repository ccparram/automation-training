package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginManager;
import pluggable.plugin.PluginRegistry;
import pluggable.plugin.events.PluginsUpdatedEvent;
import pluggable.watcher.DirectoryWatchService;
import pluggable.watcher.RegisterPluginsListener;
import pluggable.watcher.SimpleDirectoryWatchService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Juan Krzemien
 */
public class PluginManagerImpl implements PluginManager, Logging {

    private final DirectoryWatchService dirWatchService;
    private final PluginRegistry pluginsRegistry = new PluginRegistryImpl();
    private final Set<PluginsUpdatedEvent> subscribers = new HashSet<>();

    public PluginManagerImpl(Path pluginsDirectory) throws IOException {
        createPluginDirectoryIfNotExists(pluginsDirectory);
        this.dirWatchService = new SimpleDirectoryWatchService(pluginsDirectory);
        dirWatchService.register(new RegisterPluginsListener(pluginsDirectory, pluginsRegistry, subscribers));
        dirWatchService.start();
    }

    public void subscribe(PluginsUpdatedEvent onPluginsUpdated) {
        subscribers.add(onPluginsUpdated);
    }

    private boolean createPluginDirectoryIfNotExists(Path watchedPath) {
        return !watchedPath.toFile().exists() && watchedPath.toFile().mkdirs();
    }

    @Override
    public void close() throws Exception {
        dirWatchService.stop();
    }

    @Override
    public Collection<Plugin> getPlugins() {
        return pluginsRegistry.getPlugins();
    }
}
