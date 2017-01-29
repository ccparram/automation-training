package pluggable.watcher;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.PluginRegistry;

import java.nio.file.Path;

/**
 * @author Juan Krzemien
 */
public class RegisterPluginsListener implements DirectoryWatchService.OnFileChangeListener, Logging {

    private static final String JAR_SUFFIX = ".jar";

    private final PluginRegistry pluginRegistry;

    public RegisterPluginsListener(PluginRegistry pluginRegistry) {
        this.pluginRegistry = pluginRegistry;
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
