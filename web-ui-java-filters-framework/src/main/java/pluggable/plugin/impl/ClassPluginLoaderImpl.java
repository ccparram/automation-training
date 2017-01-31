package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginLoader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class ClassPluginLoaderImpl implements PluginLoader, Logging {

    @Override
    public boolean handlesExtension(String extension) {
        return "class".equalsIgnoreCase(extension);
    }

    @Override
    public Optional<Plugin> loadPlugin(Path filePath) throws IOException {
        String rootFolder = Paths.get("plugins").toFile().getAbsolutePath();
        String className = filePath.toFile().getAbsolutePath().replace(rootFolder, "").substring(1).replace(".class", "").replace("/", ".");

        try (URLClassLoader loader = new URLClassLoader(new URL[]{Paths.get("plugins").toUri().toURL()}, getClass().getClassLoader())) {
            return Optional.of(createPlugin(className, loader));
        } catch (Exception | NoClassDefFoundError e) {
            getLogger().error(format("Could not load plugin from %s. Cause: %s", filePath.toString(), e.toString()));
        }
        return Optional.empty();
    }

    private Plugin createPlugin(String pluginClassName, ClassLoader loader) throws Exception {
        Class<?> clazz = Class.forName(pluginClassName, true, loader);
        Class<? extends Plugin> pluginClass = clazz.asSubclass(Plugin.class);
        Constructor<? extends Plugin> constructor = pluginClass.getConstructor();
        return constructor.newInstance();
    }

}
