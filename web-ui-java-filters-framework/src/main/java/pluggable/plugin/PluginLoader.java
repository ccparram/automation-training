package pluggable.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Juan Krzemien
 */
public interface PluginLoader {

    boolean handlesExtension(String extension);

    Optional<Plugin> loadPlugin(Path filePath) throws IOException;

}
