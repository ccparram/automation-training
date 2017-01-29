package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginManager;
import pluggable.plugin.PluginRegistry;
import pluggable.watcher.DirectoryWatchService;
import pluggable.watcher.RegisterPluginsListener;
import pluggable.watcher.SimpleDirectoryWatchService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
public class PluginManagerImpl implements PluginManager, Logging {

    private static final String JAR_SUFFIX = ".jar";

    private final DirectoryWatchService dirWatchService;
    private final PluginRegistry pluginsRegistry = new PluginRegistryImpl();
    private final Path watchedPath;

    public PluginManagerImpl(Path pluginsDirectory) throws IOException {
        createPluginDirectoryIfNotExists(pluginsDirectory);
        this.watchedPath = pluginsDirectory;
        this.dirWatchService = new SimpleDirectoryWatchService(pluginsDirectory);
        dirWatchService.register(new RegisterPluginsListener(pluginsRegistry));
        refreshPlugins();
        dirWatchService.start();
    }

    private boolean createPluginDirectoryIfNotExists(Path watchedPath) {
        return !watchedPath.toFile().exists() && watchedPath.toFile().mkdirs();
    }

    private void refreshPlugins() {
        pluginsRegistry.unloadPlugins();
        ofNullable(watchedPath.toFile().listFiles())
                .ifPresent(files -> stream(files)
                        .map(File::toPath)
                        .filter(this::isJarFile)
                        .forEach(pluginsRegistry::register));
    }

    private boolean isJarFile(Path filePath) {
        return filePath.toString().endsWith(JAR_SUFFIX);
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
