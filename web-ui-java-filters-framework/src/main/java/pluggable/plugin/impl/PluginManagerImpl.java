package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginManager;
import pluggable.plugin.PluginRegistry;
import pluggable.plugin.events.PluginsUpdatedEvent;
import pluggable.watcher.DirectoryWatchService;
import pluggable.watcher.SimpleDirectoryWatchService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Juan Krzemien
 */
public class PluginManagerImpl implements PluginManager, DirectoryWatchService.OnFileChangeListener, Logging {

    private static final String JAR_SUFFIX = ".jar";

    private final DirectoryWatchService dirWatchService;
    private final Set<PluginsUpdatedEvent> observers = new HashSet<>();
    private final PluginRegistry pluginsRegistry = new PluginRegistryImpl();

    public PluginManagerImpl(Path pluginsDirectory) throws IOException {
        createPluginDirectoryIfNotExists(pluginsDirectory);

        pluginsRegistry.addPluginLoader(new JarPluginLoaderImpl());
        pluginsRegistry.addPluginLoader(new ClassPluginLoaderImpl());

        // Register already present plugins in directory...
        Files.walk(pluginsDirectory)
                .filter(path -> path.toFile().isFile())
                .forEach(pluginsRegistry::register);


        this.dirWatchService = new SimpleDirectoryWatchService(pluginsDirectory);
        dirWatchService.register(this);
        dirWatchService.start();
    }

    public void onPluginsUpdate(PluginsUpdatedEvent onPluginsUpdated) {
        observers.add(onPluginsUpdated);
    }

    private boolean createPluginDirectoryIfNotExists(Path watchedPath) {
        return !watchedPath.toFile().exists() && watchedPath.toFile().mkdirs();
    }

    @Override
    public void close() throws Exception {
        dirWatchService.stop();
        pluginsRegistry.unloadPlugins();
    }

    @Override
    public Collection<Plugin> getPlugins() {
        return Optional.ofNullable(pluginsRegistry.getPlugins()).orElse(Collections.emptySet());
    }

    @Override
    public void onFileCreate(Path filePath) {
        getLogger().info("File CREATED: " + filePath);
        if (isJarFile(filePath)) {
            pluginsRegistry.register(filePath);
        }
    }

    @Override
    public void onFileModify(Path filePath) {
        //getLogger().info("File MODIFIED: " + filePath);
        notifyObservers();
    }

    @Override
    public void onFileDelete(Path filePath) {
        getLogger().info("File DELETED: " + filePath);
        if (isJarFile(filePath)) {
            pluginsRegistry.unregister(filePath);
        }
    }

    private void notifyObservers() {
        observers.parallelStream().forEach(observer -> observer.pluginsUpdated(pluginsRegistry.getPlugins()));
    }

    private boolean isJarFile(Path filePath) {
        return filePath.toString().endsWith(JAR_SUFFIX);
    }

}
