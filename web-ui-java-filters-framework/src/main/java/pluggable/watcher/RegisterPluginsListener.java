package pluggable.watcher;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.PluginRegistry;
import pluggable.plugin.events.PluginsUpdatedEvent;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
public class RegisterPluginsListener implements DirectoryWatchService.OnFileChangeListener, Logging {

    private static final String JAR_SUFFIX = ".jar";

    private final PluginRegistry pluginRegistry;
    private final Set<PluginsUpdatedEvent> subscribers;

    public RegisterPluginsListener(Path watchedPath, PluginRegistry pluginsRegistry, Set<PluginsUpdatedEvent> subscribers) {
        this.pluginRegistry = pluginsRegistry;
        this.subscribers = subscribers;
        // Register already present plugins in directory...
        ofNullable(watchedPath.toFile().listFiles())
                .ifPresent(files -> stream(files)
                        .map(File::toPath)
                        .filter(this::isJarFile)
                        .forEach(pluginRegistry::register));
    }

    private void notifySubscribers() {
        for (PluginsUpdatedEvent subscriber : subscribers) {
            subscriber.pluginsUpdated(pluginRegistry.getPlugins());
        }
    }

    @Override
    public void onFileCreate(Path filePath) {
        getLogger().info("File CREATED: " + filePath);
        if (isJarFile(filePath)) {
            pluginRegistry.register(filePath);
        }
    }

    @Override
    public void onFileModify(Path filePath) {
        //getLogger().info("File MODIFIED: " + filePath);
        notifySubscribers();
    }

    @Override
    public void onFileDelete(Path filePath) {
        getLogger().info("File DELETED: " + filePath);
        if (isJarFile(filePath)) {
            pluginRegistry.unregister(filePath);
        }
    }

    private boolean isJarFile(Path filePath) {
        return filePath.toString().endsWith(JAR_SUFFIX);
    }

}
