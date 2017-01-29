package pluggable.plugin.impl;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginLoader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.jar.JarFile;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class PluginLoaderImpl implements PluginLoader, Logging {

    @Override
    public Optional<Plugin> loadPlugin(Path filePath) throws IOException {
        try (JarFile jar = new JarFile(filePath.toFile())) {
            if (isPluginJar(jar)) {
                try (URLClassLoader loader = new URLClassLoader(new URL[]{filePath.toUri().toURL()}, getClass().getClassLoader())) {
                    String pluginClassName = getPluginClassFromManifest(jar);
                    return Optional.of(createPlugin(pluginClassName, loader));
                } catch (Exception | NoClassDefFoundError e) {
                    getLogger().error(format("Could not load plugin from %s. Cause: %s", filePath.toString(), e.toString()));
                }
            } else {
                getLogger().error(format("JAR file [%s] seems not to contain a valid plugin.", filePath.toString()));
            }
        }
        return Optional.empty();
    }

    private boolean isPluginJar(JarFile jarFile) {
        try {
            return !getPluginClassFromManifest(jarFile).isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    private Plugin createPlugin(String pluginClassName, ClassLoader loader) throws Exception {
        Class<?> clazz = Class.forName(pluginClassName, true, loader);
        Class<? extends Plugin> pluginClass = clazz.asSubclass(Plugin.class);
        Constructor<? extends Plugin> constructor = pluginClass.getConstructor();
        return constructor.newInstance();
    }

    private String getPluginClassFromManifest(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue("Plugin-Class");
    }
}
