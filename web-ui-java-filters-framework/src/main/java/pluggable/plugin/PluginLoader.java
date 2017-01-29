package pluggable.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Juan Krzemien
 */
public interface PluginLoader {

    Optional<Plugin> loadPlugin(Path filePath) throws IOException;

}
