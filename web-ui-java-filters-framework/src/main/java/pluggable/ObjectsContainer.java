package pluggable;

import com.globant.automation.trainings.logging.Logging;
import pluggable.filter.FilterChain;
import pluggable.plugin.PluginManager;
import pluggable.plugin.impl.PluginManagerImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Juan Krzemien
 */
public enum ObjectsContainer implements AutoCloseable, Logging {

    INSTANCE;

    private final Map<Class<?>, Object> objects = new ConcurrentHashMap<>();

    private PluginManager pluginManager;

    ObjectsContainer() {
        try {
            Path pluginsDirectory = Paths.get("plugins");
            this.pluginManager = new PluginManagerImpl(pluginsDirectory);
        } catch (IOException e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
        pluginManager.subscribe(updatedPlugins -> {
            for (Object object : objects.values()) {
                FilterChain.processFilters(updatedPlugins, object);
            }
        });
    }

    public void add(Object object) {
        Object processedObject = FilterChain.processFilters(pluginManager.getPlugins(), object);
        objects.put(object.getClass(), processedObject);
    }

    public <T> T get(Class<T> objectType) {
        return (T) objects.get(objectType);
    }

    @Override
    public void close() throws Exception {
        pluginManager.close();
    }
}
