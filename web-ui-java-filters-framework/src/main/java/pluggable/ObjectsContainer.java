package pluggable;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginManager;
import pluggable.plugin.impl.PluginManagerImpl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public enum ObjectsContainer implements AutoCloseable, Logging {

    INSTANCE;

    private final ThreadLocal<Set<Object>> objects = ThreadLocal.withInitial(HashSet::new);

    private PluginManager pluginManager;

    ObjectsContainer() {
        try {
            this.pluginManager = new PluginManagerImpl(Paths.get("plugins"));
            pluginManager.onPluginsUpdate(updatedPlugins -> objects.get().forEach(o -> processFilters(updatedPlugins, o)));
        } catch (IOException e) {
            getLogger().error("Could not initialize PluginManager!", e);
        }
    }

    public void add(Object object) {
        Object processedObject = processFilters(pluginManager.getPlugins(), object);
        objects.get().add(processedObject);
    }

    public <T> List<T> get(Class<T> objectType) {
        return objects.get()
                .parallelStream()
                .filter(o -> objectType.isAssignableFrom(o.getClass()))
                .map(objectType::cast)
                .collect(toList());
    }

    @Override
    public void close() throws Exception {
        objects.get().clear();
        objects.remove();
        pluginManager.close();
    }

    private Object processFilters(Collection<Plugin> plugins, Object object) {
        SortedSet<Plugin> sortedSet = new ConcurrentSkipListSet<>(plugins);

        Object response = object;
        for (Plugin plugin : sortedSet) {
            // pass request & response through various filters
            response = plugin.execute(response);
        }
        getLogger().info("Processed object: " + object.toString());
        return response;
    }
}
